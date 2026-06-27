# Q008：getLoginUser 如何从请求中取 Token？

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

JWT 改造后，`getLoginUser` 不再从 Session 取用户，应如何从请求中获取 Token 并识别当前登录用户？

---

## 结论

Token 由前端放在 **HTTP 请求头** 中，后端通过 `JwtUtils` 提取并解析：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 代码流程

### 1. 从请求头取 Token 字符串

```java
String token = jwtUtils.getTokenFromRequest(request);
```

内部读取 `application.yml` 配置的 `jwt.header`（默认 `Authorization`）和 `jwt.token-prefix`（默认 `Bearer `），去掉前缀后得到纯 Token。

### 2. 解析 Token 得到 userId

```java
Long userId = jwtUtils.getUserIdFromRequest(request);
// 或分步：
// DecodedJWT jwt = jwtUtils.decodeToken(token);
// Long userId = jwt.getClaim("userId").asLong();
```

`decodeToken` 会校验签名和过期时间，无效则抛 `NOT_LOGIN_ERROR`。

### 3. 查库得到最新 User

```java
User currentUser = this.getById(userId);
```

仍以数据库为准，便于封号、改角色后立即生效。

---

## getLoginUser 完整示例（已实施）

```java
@Override
public User getLoginUser(HttpServletRequest request) {
    Long userId = jwtUtils.getUserIdFromRequest(request);
    if (userId == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    User currentUser = this.getById(userId);
    if (currentUser == null) {
        throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
    }
    if (UserRoleEnum.BAN.getValue().equals(currentUser.getUserRole())) {
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }
    return currentUser;
}
```

---

## 前端调用方式

Knife4j / Postman / Axios 需带请求头：

```
Authorization: Bearer <登录返回的 token>
```

Axios 示例：

```javascript
axios.get('/api/user/get/login', {
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
});
```

---

## Session vs JWT 对比

| Session（旧） | JWT（新） |
|---------------|-----------|
| `request.getSession().getAttribute("user_login")` | `jwtUtils.getTokenFromRequest(request)` |
| 浏览器自动带 Cookie | 前端手动带 Authorization 头 |
| 服务端存登录态 | Token 自包含 userId，服务端无 Session |

---

## 相关文件

- `JwtUtils.java` — `getTokenFromRequest`、`getUserIdFromRequest`、`decodeToken`
- `UserServiceImpl.java` — `getLoginUser`、`getLoginUserPermitNull`
- `application.yml` — `jwt.header`、`jwt.token-prefix`
