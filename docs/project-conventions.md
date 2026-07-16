# 项目开发约定

本文件约定本仓库的目录与维护方式，协作与 AI 辅助开发时请遵守。

---

## SQL / 数据库

| 规则 | 说明 |
|------|------|
| **唯一建表文件** | 所有表结构 DDL（`CREATE TABLE` / 索引变更等）只维护在 [`sql/create_table.sql`](../sql/create_table.sql) |
| **禁止拆分建表文件** | 不要新增 `sql/create_xxx_table.sql`；新表、改表一律追加到 `create_table.sql` |
| **种子数据** | 可选，使用 `sql/seed_*.sql`（如 `seed_article_category.sql`），与建表文件分离 |
| **非 MySQL DDL** | 如 ES mapping（`post_es_mapping.json`）可单独放在 `sql/`，不属于建表约定范围 |
| **目录说明** | 见 [`sql/README.md`](../sql/README.md) |

执行示例：

```bash
mysql -uroot -p --default-character-set=utf8mb4 < sql/create_table.sql
```

---

## 文档 / 问题集

- 对话中提出的问题：在 [`docs/project-issues.md`](./project-issues.md) 追加一行，并新建 `docs/issues/Qxxx-*.md`
- 新增设计文档：使用英文小写 + 连字符命名，并在 [`docs/README.md`](./README.md) 索引中登记

---

## 包结构（简要）

- 按技术层分包：`controller` / `service` / `mapper` / `model` 平铺即可（见 [Q024](./issues/Q024-controller-package-structure.md)）
- 代码生成器输出可先落到 `generator/`，再移动到 `src/main/java`
