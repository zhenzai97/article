# Q005：JwtUtils.generateToken 如何读取 application.yml 中的 jwt 配置

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

```java
String token = JwtUtils.generateToken(user.getId(), user.getUserRole());
```

这里怎么读取配置文件 `application.yml` 里的 `jwt` 配置（`secret`、`expire` 等）？

---

## 原因说明

`JwtUtils` 若是 **纯静态工具类**，无法直接使用 Spring 的 `@Value` / `@ConfigurationProperties` 注入配置，因为静态方法不在 Spring 容器管理范围内。

Spring Boot 读取自定义 `yaml` 配置的常规做法：

1. 定义配置类 + `@ConfigurationProperties(prefix = "jwt")`
2. 由 Spring 容器注入到 **Service / Component** 中使用

本项目 `cos.client.*` 已用同样方式（`CosClientConfig`）。

---

## 解决方案（已实施）

### 1. 配置类 `JwtConfig`

```java
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret;
    private Long expire;
    private String header = "Authorization";
    private String tokenPrefix = "Bearer ";
}
```

`application.yml` 中：

```yaml
jwt:
  secret: your-256-bit-secret-key-change-in-production
  expire: 86400000
  header: Authorization
  token-prefix: "Bearer "
```

YAML 的 `token-prefix` 会自动映射到 Java 字段 `tokenPrefix`（松散绑定）。

### 2. `JwtUtils` 改为 Spring 组件并注入 `JwtConfig`

```java
@Component
public class JwtUtils {

    @Resource
    private JwtConfig jwtConfig;

    public String generateToken(Long userId, String userRole) {
        return generateToken(userId, userRole, jwtConfig.getSecret(), jwtConfig.getExpire());
    }
}
```

### 3. `UserServiceImpl` 注入 `JwtUtils` 使用

```java
@Resource
private JwtUtils jwtUtils;

// 登录成功后
String token = jwtUtils.generateToken(user.getId(), user.getUserRole());
LoginUserVO loginUserVO = this.getLoginUserVO(user);
loginUserVO.setToken(token);
return loginUserVO;
```

**注意**：不能写 `JwtUtils.generateToken(...)` 静态调用，应注入 `jwtUtils` 后调用实例方法。

---

## 其他读取方式（了解即可）

| 方式 | 示例 | 适用场景 |
|------|------|----------|
| `@ConfigurationProperties` | 本方案 | 一组相关配置，推荐 |
| `@Value` | `@Value("${jwt.secret}") String secret` | 单个字段 |
| `Environment` | `environment.getProperty("jwt.secret")` | 动态读取 |

---

## 相关文件

- `src/main/java/com/springbootinit/config/JwtConfig.java`
- `src/main/java/com/springbootinit/utils/JwtUtils.java`
- `src/main/java/com/springbootinit/service/impl/UserServiceImpl.java`
- `src/main/resources/application.yml`
