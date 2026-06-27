# Q004：application.yml 中 jwt.secret 提示「无法解析」

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

在 `application.yml` 中配置：

```yaml
jwt:
  secret: your-256-bit-secret-key-change-in-production
  expire: 86400000
  header: Authorization
  token-prefix: "Bearer "
```

IDE（IntelliJ / Cursor）提示：**无法解析配置属性 `jwt.secret`**（同类警告也可能出现在 `jwt.expire` 等字段上）。

---

## 原因分析

这是 **IDE 静态检查提示**，不是 Spring Boot 启动失败。

### 根本原因

`jwt.*` 属于项目 **自定义配置前缀**，并非 Spring Boot 官方内置属性（如 `spring.datasource.url`）。

IDE 的 Spring Boot 配置助手通过以下方式识别合法配置项：

1. **官方** `spring-boot-configuration-metadata.json`（仅包含 Spring 自带属性）
2. **项目自定义** `META-INF/additional-spring-configuration-metadata.json`
3. **代码绑定** `@ConfigurationProperties(prefix = "jwt")` 类（配合 `spring-boot-configuration-processor`）

本项目原先已在 `additional-spring-configuration-metadata.json` 中注册了 `cos.*`、`wx.open.*`，但 **未注册 `jwt.*`**，因此 IDE 无法识别，显示黄色警告。

### 与运行时无关

只要代码中通过 `@Value("${jwt.secret}")` 或 `JwtProperties` 正确读取，**应用仍可正常启动**；警告仅影响编辑体验（无自动补全、无类型提示）。

### 易混淆点

`wx.mp.secret` 是微信配置，`jwt.secret` 是 JWT 签名密钥，二者不同；若只配置了 `jwt` 而未注册元数据，只有 `jwt` 前缀会报无法解析。

---

## 解决方案

### 方案 A：注册配置元数据（推荐，本项目已采用）

在 `src/main/resources/META-INF/additional-spring-configuration-metadata.json` 中追加：

```json
{
  "name": "jwt.secret",
  "type": "java.lang.String",
  "description": "JWT 签名密钥，生产环境务必替换为足够长的随机字符串。"
},
{
  "name": "jwt.expire",
  "type": "java.lang.Long",
  "description": "Access Token 过期时间（毫秒）。"
},
{
  "name": "jwt.header",
  "type": "java.lang.String",
  "description": "Token 请求头名称，默认 Authorization。"
},
{
  "name": "jwt.token-prefix",
  "type": "java.lang.String",
  "description": "Token 前缀，默认 Bearer （含末尾空格）。"
}
```

保存后 **重新构建项目** 或 **Invalidate Caches / 重启 IDE**，警告应消失。

这与项目中 `cos.client.*`、`wx.open.*` 的处理方式一致。

### 方案 B：新增 `@ConfigurationProperties` 类（改造 JWT 时建议一并做）

```java
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private Long expire;
    private String header = "Authorization";
    private String tokenPrefix = "Bearer ";
}
```

在启动类或配置类上 `@EnableConfigurationProperties(JwtProperties.class)`，并确保 `pom.xml` 中 `spring-boot-configuration-processor` 为 optional 依赖（父 POM 通常已包含），编译时会生成额外元数据。

### 方案 C：忽略警告

若暂不处理，可忽略；不影响 `application.yml` 被 Spring 正常加载。

---

## 验证

1. 修改元数据后执行 `mvn compile`
2. 在 IDE 中打开 `application.yml`，`jwt.secret` 不应再显示无法解析
3. 启动应用，确认无 `Could not resolve placeholder 'jwt.secret'` 类错误（若代码已引用该配置）

---

## 相关文件

- `src/main/resources/application.yml`（第 103～111 行）
- `src/main/resources/META-INF/additional-spring-configuration-metadata.json`（已补充 jwt 项）
- 参考同文件中的 `cos.client.*`、`wx.open.*` 注册方式

---

## 参考

- [Spring Boot Custom Configuration Metadata](https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata.annotation-processor)
