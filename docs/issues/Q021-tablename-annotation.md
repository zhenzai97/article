# Q021：@TableName(value = "article_category") 注解的作用

- **日期**：2026-07-15
- **状态**：已解答

---

## 问题描述

`ArticleCat` 实体上的注解 `@TableName(value = "article_category")` 是干什么用的？

---

## 原因分析

MyBatis-Plus 默认按**实体类名**推断表名（驼峰转下划线）：

- 类名 `ArticleCat` → 推断表名 `article_cat`

但本项目实际建表名为 `article_category`，二者不一致。若不显式指定，CRUD 会查错表或报「表不存在」。

---

## 解决方案 / 结论

`@TableName` 用于**把实体类绑定到真实数据库表名**：

```java
@TableName(value = "article_category")
public class ArticleCat implements Serializable {
    // ...
}
```

| 项 | 说明 |
|----|------|
| 注解 | `@TableName`（MyBatis-Plus） |
| `value` | 数据库中的真实表名 |
| 效果 | `save` / `update` / `list` 等操作都针对 `article_category` 表 |

**何时需要**：实体类名（或下划线转换结果）与表名不一致时；名称一致时可不写，但写上更直观、更稳妥。

---

## 相关文件

- `src/main/java/com/springbootinit/model/entity/ArticleCat.java`
- `sql/create_table.sql`（`article_category` 建表语句）
