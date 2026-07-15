# Q023：ArticleCatController 上 @RestController、@RequestMapping、@Slf4j 的作用

- **日期**：2026-07-15
- **状态**：已解答

---

## 问题描述

```java
@RestController
@RequestMapping("/articleCat")
@Slf4j
public class ArticleCatController {
```

这三个注解分别是干什么的？

---

## 解决方案 / 结论

### 1. `@RestController`（Spring）

标识该类是 **REST 风格控制器**。

等价于：

- `@Controller`：注册为 Spring MVC 控制器，能处理 HTTP 请求
- `@ResponseBody`：方法返回值直接写入响应体（通常序列化为 JSON），而不是解析成视图名

本项目接口返回 `BaseResponse<...>`，由 Jackson 转成 JSON，所以用 `@RestController`。

### 2. `@RequestMapping("/articleCat")`（Spring）

给控制器指定 **统一路径前缀**。

配合 `application.yml` 中的 `server.servlet.context-path: /api`，实际路径例如：

| 方法上的映射 | 完整 URL |
|--------------|----------|
| `@PostMapping("/add")` | `POST /api/articleCat/add` |
| `@PostMapping("/list/page/vo")` | `POST /api/articleCat/list/page/vo` |
| `@DeleteMapping("/delete/{id}")` | `DELETE /api/articleCat/delete/{id}` |

也可写在方法上；写在类上可避免每个方法重复写 `/articleCat`。

### 3. `@Slf4j`（Lombok）

编译期自动生成日志对象：

```java
private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ArticleCatController.class);
```

类里可直接写：

```java
log.info("创建分类成功, id={}", id);
log.error("操作失败", e);
```

无需手写 `Logger` 定义。

---

## 三个注解对比

| 注解 | 来源 | 作用 |
|------|------|------|
| `@RestController` | Spring | 声明 REST 控制器，返回 JSON |
| `@RequestMapping("/articleCat")` | Spring | 类级别路径前缀 |
| `@Slf4j` | Lombok | 自动注入 `log` 日志对象 |

---

## 相关文件

- `src/main/java/com/springbootinit/controller/ArticleCatController.java`
- `src/main/resources/application.yml`（`context-path: /api`）
