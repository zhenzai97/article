# Q025：建表 SQL 只保留 create_table.sql 的约定

- **日期**：2026-07-16
- **状态**：已解答

---

## 问题描述

`sql/` 目录只用保留 `create_table.sql`，以后生产的表 SQL 都追加到这里，并写入项目约定。

---

## 结论

已落实：

1. **删除**拆分的建表文件：
   - `sql/create_article_table.sql`
   - `sql/create_advertising_space_table.sql`
   - `sql/create_advertising_table.sql`
2. **唯一 DDL**：`sql/create_table.sql`（文件头已注明约定）
3. **约定文档**：[`docs/project-conventions.md`](../project-conventions.md)
4. **Cursor 规则**：`.cursor/rules/sql-create-table.mdc`（编辑 `sql/**` 时生效）
5. **文档索引**：`docs/README.md` 已链到约定

### 保留说明

| 文件类型 | 是否保留 | 说明 |
|----------|----------|------|
| `create_table.sql` | ✅ 必须 | 唯一建表 / DDL |
| `seed_*.sql` | ✅ 可保留 | 种子数据，不是建表 DDL |
| `post_es_mapping.json` | ✅ 可保留 | ES 映射，非 MySQL 建表 |

---

## 相关文件

- `sql/create_table.sql`
- `docs/project-conventions.md`
- `docs/README.md`
- `.cursor/rules/sql-create-table.mdc`
