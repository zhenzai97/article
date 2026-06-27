# Q006：API 返回时间格式改为 yyyy-MM-dd HH:mm:ss

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

登录接口返回示例：

```json
"createTime": "2026-06-26T03:38:07.000+00:00",
"updateTime": "2026-06-26T03:38:07.000+00:00"
```

希望改为易读的 `YYYY-MM-DD HH:mm:ss` 格式，应在哪里配置？

---

## 原因说明

Jackson 默认将 `java.util.Date` 序列化为 **ISO-8601** 字符串（带 `T` 和时区），例如 `2026-06-26T03:38:07.000+00:00`。

本项目实体、VO（`User`、`LoginUserVO`、`PostVO` 等）的时间字段均为 `Date` 类型，若在每个 VO 上单独加 `@JsonFormat` 会重复且难维护。

---

## 推荐方案：全局配置（已采用）

在 **`JsonConfig.java`** 中统一设置（与现有 Long 转 String 配置同一处）：

```java
objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
```

**优点**：

- 一处修改，所有接口的 `Date` 字段统一格式
- 与项目已有 JSON 全局配置风格一致
- 东八区显示，符合国内阅读习惯

改造后示例：

```json
"createTime": "2026-06-26 11:38:07",
"updateTime": "2026-06-26 11:38:07"
```

（UTC 03:38 对应北京时间 11:38）

---

## 其他可选方案（对比）

| 方案 | 位置 | 适用场景 |
|------|------|----------|
| **全局 ObjectMapper** | `JsonConfig` | 全项目 `Date` 统一格式，**推荐** |
| `application.yml` | `spring.jackson.date-format` + `time-zone` | 不配自定义 `ObjectMapper Bean` 时可用 |
| `@JsonFormat` 字段级 | 单个 VO 字段 | 仅个别字段需要特殊格式 |
| 返回 `String` 类型时间 | VO 层手动格式化 | 不推荐，丢失类型语义 |

`application.yml` 示例（备选，与 JsonConfig 二选一，避免重复配置）：

```yaml
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
```

本项目已自定义 `ObjectMapper` Bean，因此在 `JsonConfig` 中设置更直接。

---

## 注意

- 格式应使用 **`HH`**（24 小时），不要用 `hh`（12 小时）
- 仅影响 **JSON 输出**；数据库、MyBatis 读写不受影响
- ES 相关 `PostEsDTO` 若有独立格式，需单独检查

---

## 相关文件

- `src/main/java/com/springbootinit/config/JsonConfig.java`
