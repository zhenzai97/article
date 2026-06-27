# Session 登录态改造 JWT Token 方案

> 文档版本：v1.0  
> 适用项目：`springboot-init`（`com.springbootinit`）  
> 编写日期：2026-06-26  
> 状态：待实施

---

## 一、改造背景与目标

### 1.1 现状（Session）

当前登录认证基于 **HttpSession**：

- 登录成功后将完整 `User` 实体写入 Session，Key 为 `user_login`（`UserConstant.USER_LOGIN_STATE`）
- 后续请求通过 `request.getSession().getAttribute(USER_LOGIN_STATE)` 获取登录用户
- Session 默认 30 天过期（`application.yml` 中 `spring.session.timeout` 与 cookie `max-age`）
- 跨域配置开启 `allowCredentials: true`，依赖浏览器 Cookie 携带 `JSESSIONID`

**核心代码位置**：

| 文件 | 职责 |
|------|------|
| `UserServiceImpl` | `userLogin` / `userLoginByMpOpen` 写入 Session；`getLoginUser` / `userLogout` 读写 Session |
| `AuthInterceptor` | 权限校验时调用 `userService.getLoginUser(request)` |
| 各 Controller | 业务接口中调用 `getLoginUser` / `getLoginUserPermitNull` |
| `CorsConfig` | 允许跨域携带 Cookie |
| `application.yml` | Session 超时、Redis Session（可选）配置 |

### 1.2 目标（JWT）

改为 **JWT（JSON Web Token）** 无状态认证：

- 登录成功后服务端签发 Token，返回给前端
- 前端在请求头携带 Token，服务端解析并校验
- 不依赖服务端 Session 存储，天然支持多端、跨域 API 调用
- 保持现有 `@AuthCheck`、角色体系（user / admin / ban）、错误码行为一致

### 1.3 改造原则

1. **最小侵入**：`getLoginUser(HttpServletRequest)` 等对外方法签名尽量不变，Controller 层改动尽量少
2. **安全优先**：Token 载荷只存必要字段（userId、userRole），敏感信息仍从数据库查询
3. **可演进**：预留 Redis 黑名单，支持主动注销（JWT 本身无法服务端失效）
4. **前后端协同**：明确 Token 传递方式与登录接口响应变更

---

## 二、现状架构分析

### 2.1 登录态流转（Session）

```
前端 POST /user/login
    ↓
UserServiceImpl.userLogin()
    ↓
request.getSession().setAttribute("user_login", user)
    ↓
响应 Set-Cookie: JSESSIONID=...
    ↓
后续请求自动携带 Cookie
    ↓
getLoginUser() → Session 取 user → DB 再查一次（校验用户仍存在）
```

### 2.2 依赖登录态的调用点

#### 直接读写 Session（必须改造）

| 类 | 方法 | 说明 |
|----|------|------|
| `UserServiceImpl` | `userLogin` | `setAttribute(USER_LOGIN_STATE, user)` |
| `UserServiceImpl` | `userLoginByMpOpen` | 同上 |
| `UserServiceImpl` | `getLoginUser` | `getAttribute` + DB 查询 |
| `UserServiceImpl` | `getLoginUserPermitNull` | 同上，未登录返回 null |
| `UserServiceImpl` | `isAdmin(request)` | 从 Session 取用户判断角色 |
| `UserServiceImpl` | `userLogout` | `removeAttribute` |

#### 通过 UserService 间接依赖（改造 getLoginUser 后自动生效）

| 类 | 说明 |
|----|------|
| `AuthInterceptor` | `@AuthCheck` 权限拦截 |
| `UserController` | 登录、注销、获取当前用户、更新个人信息等 |
| `PostController` | 发帖、删帖、编辑、我的帖子等 |
| `PostThumbController` | 点赞 |
| `PostFavourController` | 收藏 |
| `FileController` | 文件上传 |
| `PostServiceImpl` | `getPostVO` / `getPostVOPage` 判断点赞收藏状态 |

#### 不受本次改造影响

| 类 | 说明 |
|----|------|
| `WxMpController` / `wxmp.handler.*` | 微信 `WxSessionManager` 为微信 SDK 概念，与 HTTP Session 无关 |
| `application.yml` 中 `wx.mp.token` | 微信公众号服务器配置 Token，非 JWT |

### 2.3 当前登录接口响应

`LoginUserVO` 当前字段：`id`、`userName`、`userAvatar`、`userProfile`、`userRole`、`createTime`、`updateTime`  
**不包含 Token**，改造后需扩展。

---

## 三、目标架构设计

### 3.1 JWT 认证流转

```
前端 POST /user/login
    ↓
UserServiceImpl.userLogin()
    ↓
校验账号密码 → JwtUtils.generateToken(userId, userRole)
    ↓
LoginUserVO（含 token 字段）返回给前端
    ↓
前端存储 token（localStorage / sessionStorage）
    ↓
后续请求 Header: Authorization: Bearer <token>
    ↓
JwtInterceptor（或 Filter）解析 Token → 写入 Request 属性（可选）
    ↓
getLoginUser(request) → 从 Header 取 Token → 校验签名/过期 → DB 查用户
    ↓
@AuthCheck / 业务逻辑
```

### 3.2 Token 载荷设计（Claims）

建议 **最小载荷**，避免把完整 User 放进 JWT：

| Claim | 类型 | 说明 |
|-------|------|------|
| `userId` | Long | 用户主键 |
| `userRole` | String | 角色：`user` / `admin` / `ban` |
| `iat` | Date | 签发时间（库自动生成） |
| `exp` | Date | 过期时间（库自动生成） |

**不在 JWT 中存储**：密码、unionId、mpOpenId 等敏感或可变字段。角色变更、封号等以 **数据库实时查询** 为准（与现有 `getLoginUser` 回查 DB 逻辑一致）。

### 3.3 Token 传递约定

| 项 | 约定 |
|----|------|
| 请求头 | `Authorization: Bearer <token>` |
| 备用（可选） | 查询参数 `token=`，仅用于特殊场景（如部分下载链接），不推荐作为主方案 |
| 响应体 | 登录、微信登录接口在 `LoginUserVO` 中返回 `token` 字段 |

### 3.4 过期与刷新策略（推荐）

| 策略 | 有效期 | 用途 |
|------|--------|------|
| Access Token | 24 小时（可配置） | 日常 API 调用 |
| Refresh Token（可选二期） | 30 天 | 无感续期，降低 Access Token 泄露风险 |

**一期建议**：仅实现 Access Token + 配置化过期时间（默认 24h 或 7d），与原先 30 天 Session 可酌情对齐。

### 3.5 注销（Logout）策略

JWT 无状态，服务端无法直接「删除」已签发的 Token。可选方案：

| 方案 | 实现 | 适用 |
|------|------|------|
| **A. 纯客户端注销** | 前端删除本地 Token | 最简单，Token 在过期前仍有效 |
| **B. Redis 黑名单（推荐）** | `logout` 时将 Token 加入 Redis，校验时检查 | 需 Redis，支持主动失效 |
| **C. 短过期 + Refresh Token** | Access Token 短有效期 | 安全性高，实现复杂 |

**推荐一期**：方案 A；若项目已计划启用 Redis，同步实现方案 B。

---

## 四、技术选型

### 4.1 JWT 库

推荐使用 **Auth0 java-jwt**（轻量、文档清晰）：

```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
```

备选：`io.jsonwebtoken:jjwt`（0.12.x），功能相当。

### 4.2 签名算法

- 使用 **HMAC256**（`Algorithm.HMAC256(secret)`）
- `secret` 从配置文件读取，**禁止硬编码**，生产环境使用足够长的随机字符串（≥ 32 字符）

### 4.3 新增类清单（建议）

| 类 | 包路径 | 职责 |
|----|--------|------|
| `JwtProperties` | `config` | 读取 `jwt.secret`、`jwt.expire` 等配置 |
| `JwtUtils` | `utils` | 生成 Token、解析 Token、校验过期 |
| `JwtInterceptor` | `aop` 或 `interceptor` | 可选：统一从 Header 解析并设置上下文 |
| `WebMvcConfig` | `config` | 注册拦截器、配置放行路径 |

---

## 五、详细改造步骤

### 阶段一：依赖与配置

#### 5.1 添加 Maven 依赖

在 `pom.xml` 的 `<dependencies>` 中加入 `java-jwt`（见 4.1）。

#### 5.2 新增 JWT 配置

在 `application.yml` 增加：

```yaml
jwt:
  # 签名密钥（生产环境务必替换，且不要提交到公开仓库）
  secret: your-256-bit-secret-key-change-in-production
  # Access Token 过期时间（毫秒），示例：24 小时
  expire: 86400000
  # Token 请求头名称（可选，默认 Authorization）
  header: Authorization
  # Token 前缀（可选，默认 Bearer ）
  token-prefix: "Bearer "
```

各环境可在 `application-prod.yml` 覆盖 `secret` 与 `expire`。

#### 5.3 清理或保留 Session 配置

JWT 改造完成后可 **移除或注释**：

```yaml
spring:
  session:
    timeout: 2592000
server:
  servlet:
    session:
      cookie:
        max-age: 2592000
```

若不再使用 Redis Session，可保持 `MainApplication` 排除 `RedisAutoConfiguration`（与登录无关）。

---

### 阶段二：核心工具类

#### 5.4 实现 `JwtUtils`

核心方法建议：

```java
// 生成 Token
public static String generateToken(Long userId, String userRole);

// 从 Token 解析 userId（校验签名与过期）
public static DecodedJWT decodeToken(String token);

// 从 HttpServletRequest 提取 Token（处理 Bearer 前缀）
public static String getTokenFromRequest(HttpServletRequest request);
```

**Token 提取逻辑**：

1. 读取 `Authorization` 头
2. 若以 `Bearer ` 开头，去掉前缀
3. 为空则返回 null（由 `getLoginUserPermitNull` 处理）

#### 5.5 扩展 `LoginUserVO`

新增字段：

```java
/**
 * 登录凭证
 */
private String token;
```

登录、微信登录接口在返回 VO 前设置 `loginUserVO.setToken(token)`。

---

### 阶段三：改造 UserServiceImpl

#### 5.6 `userLogin` 改造

**改造前**：

```java
request.getSession().setAttribute(USER_LOGIN_STATE, user);
return this.getLoginUserVO(user);
```

**改造后**：

```java
String token = JwtUtils.generateToken(user.getId(), user.getUserRole());
LoginUserVO loginUserVO = this.getLoginUserVO(user);
loginUserVO.setToken(token);
return loginUserVO;
```

方法签名可移除对 Session 的依赖；`HttpServletRequest request` 参数可保留（避免改 Controller）或后续精简。

#### 5.7 `userLoginByMpOpen` 改造

与 `userLogin` 相同：生成 Token 写入 `LoginUserVO`，删除 `setAttribute`。

#### 5.8 `getLoginUser` 改造

**改造前**：从 Session 取 `User` → DB 查询

**改造后**：

```java
String token = JwtUtils.getTokenFromRequest(request);
if (StringUtils.isBlank(token)) {
    throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
}
DecodedJWT jwt = JwtUtils.decodeToken(token);  // 内部校验签名与过期
Long userId = jwt.getClaim("userId").asLong();
User currentUser = this.getById(userId);
if (currentUser == null) {
    throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
}
// 封号用户：即使 Token 有效也拒绝（与 AuthInterceptor 逻辑一致）
if (UserRoleEnum.BAN.getValue().equals(currentUser.getUserRole())) {
    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
}
// 可选：Redis 黑名单检查
return currentUser;
```

#### 5.9 `getLoginUserPermitNull` 改造

Token 为空或解析失败时返回 `null`，不抛异常。

#### 5.10 `isAdmin(request)` 改造

改为调用 `getLoginUserPermitNull`，或从 Token Claims 读取 `userRole`（仍建议以 DB 为准）。

#### 5.11 `userLogout` 改造

**方案 A（纯客户端）**：

```java
// 服务端直接返回成功，前端删除 Token
return true;
```

**方案 B（Redis 黑名单）**：

```java
String token = JwtUtils.getTokenFromRequest(request);
if (StringUtils.isBlank(token)) {
    throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
}
// redis.set("jwt:blacklist:" + token, "1", 剩余过期时间)
return true;
```

在 `getLoginUser` 中增加黑名单校验。

---

### 阶段四：拦截器与放行路径（可选但推荐）

#### 5.12 新增 `JwtInterceptor` + `WebMvcConfig`

在 `getLoginUser` 已能自行解析 Token 的情况下，拦截器 **不是必须**，但可用于：

- 统一记录当前用户 ID 到 MDC 日志
- 对无 `@AuthCheck` 但需要登录的接口提前校验（当前项目多在 Controller 内手动调用 `getLoginUser`）

**放行路径**（无需 Token）：

| 路径 | 说明 |
|------|------|
| `/user/login` | 账号登录 |
| `/user/register` | 注册 |
| `/user/login/wx_open` | 微信登录 |
| `/user/get/vo` | 公开用户信息 |
| `/user/list/page/vo` | 公开用户列表 |
| `/post/get/vo` | 公开帖子详情 |
| `/post/list/page/vo` | 公开帖子列表 |
| `/post/search/page/vo` | ES 搜索 |
| `/doc.html`、`/swagger-resources/**`、`/v2/api-docs` 等 | 接口文档 |
| `/`（微信） | 微信服务器回调 |

公开接口仍可通过 `getLoginUserPermitNull` 识别「已登录用户」以展示点赞状态等，**不应**在拦截器层强制要求 Token。

---

### 阶段五：跨域与前端协同

#### 5.13 调整 `CorsConfig`

JWT 方案下 **不再依赖 Cookie**，可调整：

```java
.allowCredentials(false)  // 或保持 true，不影响 Bearer Token
.allowedOriginPatterns("*")
.allowedHeaders("*")    // 确保允许 Authorization 头
.exposedHeaders("*")
```

若前端不再使用 Cookie，可将 `allowCredentials` 设为 `false`，跨域配置更简单。

#### 5.14 前端改造要点

| 项 | Session 方式 | JWT 方式 |
|----|--------------|----------|
| 登录后 | 依赖 Cookie 自动携带 | 保存 `data.token` |
| 请求头 | 无需特殊处理 | `Authorization: Bearer ${token}` |
| 跨域 | 需 `withCredentials: true` | 普通跨域即可 |
| 注销 | `POST /user/logout` + Cookie 失效 | 删除本地 Token + 调用 logout（若用黑名单） |
| 获取当前用户 | `GET /user/get/login` | 同上，需带 Token |

**Axios 示例**：

```javascript
// 登录后
localStorage.setItem('token', response.data.data.token);

// 全局请求拦截
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 注销
localStorage.removeItem('token');
await axios.post('/api/user/logout');
```

---

### 阶段六：常量与清理

#### 5.15 `UserConstant.USER_LOGIN_STATE`

JWT 改造完成后该常量 **不再使用**，可删除或保留注释说明历史用途。

#### 5.16 代码生成器模板

同步更新 FreeMarker 模板，避免新生成代码仍假设 Session：

- `src/main/resources/templates/TemplateController.java.ftl`
- `src/main/resources/templates/TemplateServiceImpl.java.ftl`

---

## 六、接口变更说明

### 6.1 响应变更

| 接口 | 变更 |
|------|------|
| `POST /user/login` | `LoginUserVO` 新增 `token` 字段 |
| `GET /user/login/wx_open` | 同上 |
| 其他接口 | 响应结构不变，但需在 Header 带 Token |

### 6.2 请求变更

除公开接口外，需登录的接口增加：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 6.3 错误码（保持不变）

| 场景 | 错误码 |
|------|--------|
| 未带 Token / Token 无效 / 过期 | `40100` NOT_LOGIN_ERROR |
| 无权限 / 封号 | `40101` NO_AUTH_ERROR |

---

## 七、安全注意事项

1. **密钥管理**：`jwt.secret` 仅放配置文件或环境变量，生产使用强随机密钥
2. **HTTPS**：生产环境必须使用 HTTPS，防止 Token 被窃听
3. **载荷最小化**：JWT 可解码，勿存密码等敏感信息
4. **过期时间**：不宜过长；敏感操作建议缩短有效期
5. **XSS**：Token 存 localStorage 时须防 XSS；可考虑 httpOnly Cookie 存 Token（需额外设计）
6. **封号实时生效**：必须在 `getLoginUser` 中查 DB，不能仅信任 Token 中的 `userRole`
7. **时钟偏移**：校验 `exp` 时可预留少量 leeway（如 60 秒）

---

## 八、测试计划

### 8.1 单元测试

| 测试项 | 说明 |
|--------|------|
| `JwtUtilsTest` | 生成、解析、过期 Token、错误签名 |
| `UserServiceTest` | 改造 `userLogin`、`getLoginUser`、`userLogout` |

### 8.2 接口测试（Knife4j / Postman）

| 场景 | 预期 |
|------|------|
| 注册 → 登录 | 响应含 `token` |
| 带 Token 访问 `GET /user/get/login` | 成功 |
| 不带 Token 访问需登录接口 | `40100` |
| 错误 / 过期 Token | `40100` |
| 管理员 `@AuthCheck` 接口 | admin Token 成功，user Token 返回 `40101` |
| 封号用户 Token | `40101` |
| 点赞 / 收藏 / 发帖 | 带 Token 正常 |
| 公开帖子列表（未登录） | 正常，`hasThumb` 等为 false |
| 公开帖子列表（已登录带 Token） | `hasThumb` 等正确 |
| 注销后（黑名单方案） | 原 Token 不可用 |
| 微信登录 | 返回 Token，后续请求正常 |

### 8.3 回归范围

- 全部 6 个 Controller 的需登录接口
- `AuthInterceptor` 管理员权限
- `PostServiceImpl` 点赞收藏状态展示

---

## 九、实施排期建议

| 阶段 | 内容 | 预估 |
|------|------|------|
| 1 | 依赖、配置、`JwtUtils`、`LoginUserVO` 扩展 | 0.5 天 |
| 2 | `UserServiceImpl` 核心改造 | 0.5 天 |
| 3 | 可选拦截器、Cors 调整、常量清理 | 0.25 天 |
| 4 | 单元测试 + 接口测试 | 0.5 天 |
| 5 | 前端联调、文档更新 | 0.5 天 |
| 6（可选） | Redis Token 黑名单 | 0.5 天 |

**合计**：约 2～2.5 个工作日（不含 Refresh Token 二期）。

---

## 十、改造文件清单（Checklist）

### 新增文件

- [ ] `src/main/java/com/springbootinit/config/JwtProperties.java`
- [ ] `src/main/java/com/springbootinit/utils/JwtUtils.java`
- [ ] `src/main/java/com/springbootinit/config/WebMvcConfig.java`（若使用拦截器）
- [ ] `src/main/java/com/springbootinit/aop/JwtInterceptor.java`（可选）
- [ ] `src/test/java/com/springbootinit/utils/JwtUtilsTest.java`

### 修改文件

- [ ] `pom.xml` — 添加 `java-jwt` 依赖
- [ ] `application.yml` — JWT 配置；可选移除 Session 配置
- [ ] `application-prod.yml` — 生产 JWT secret
- [ ] `LoginUserVO.java` — 新增 `token` 字段
- [ ] `UserServiceImpl.java` — 登录/注销/获取用户核心逻辑
- [ ] `UserConstant.java` — 移除或废弃 `USER_LOGIN_STATE`
- [ ] `CorsConfig.java` — 按需调整 `allowCredentials`
- [ ] `templates/*.ftl` — 代码生成器模板（可选）

### 无需修改（改造 getLoginUser 后自动适配）

- [ ] `AuthInterceptor.java`
- [ ] `PostController.java`、`PostThumbController.java`、`PostFavourController.java`、`FileController.java`
- [ ] `PostServiceImpl.java`

---

## 十一、回滚方案

若改造后需回滚 Session：

1. 恢复 `UserServiceImpl` 中 Session 相关代码
2. 移除 JWT 依赖与配置
3. 前端恢复 Cookie + `withCredentials`
4. `LoginUserVO` 移除 `token` 字段

建议在独立分支开发，通过功能开关 `auth.type: session | jwt` 做过渡（可选，增加复杂度）。

---

## 十二、与项目其他模块的关系

| 模块 | 影响 |
|------|------|
| Redis Session | JWT 改造后通常 **不再需要** Redis Session；Redis 仍可用于黑名单、微信配置等 |
| Elasticsearch / COS / 微信 | 无直接影响 |
| Knife4j | 可在文档中配置全局 `Authorization` 头，方便调试 |

---

## 附录 A：`JwtUtils` 参考实现骨架

```java
public class JwtUtils {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USER_ROLE = "userRole";

    public static String generateToken(Long userId, String userRole, String secret, long expireMs) {
        Date expireAt = new Date(System.currentTimeMillis() + expireMs);
        return JWT.create()
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_USER_ROLE, userRole)
                .withExpiresAt(expireAt)
                .sign(Algorithm.HMAC256(secret));
    }

    public static DecodedJWT decodeToken(String token, String secret) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "Token 无效或已过期");
        }
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
```

> 实际实现中建议通过 `JwtProperties` 注入 `secret` 与 `expire`，避免静态配置。

---

## 附录 B：Session vs JWT 对比

| 维度 | Session | JWT |
|------|---------|-----|
| 状态 | 服务端有状态 | 无状态（黑名单除外） |
| 扩展性 | 需 Redis 做分布式 Session | 天然支持多实例 |
| 跨域 | 依赖 Cookie，配置复杂 | Header 传递，简单 |
| 注销 | 服务端立即失效 | 需黑名单或短过期 |
| 移动端 | Cookie 支持一般 | 友好 |
| 载荷大小 | Session 可存完整对象 | 不宜过大 |
| 安全 | Cookie httpOnly 可防 XSS 读取 | 需防 XSS + HTTPS |

---

*文档维护：改造实施完成后，请更新本文档状态为「已实施」，并补充实际配置项与联调结论。*
