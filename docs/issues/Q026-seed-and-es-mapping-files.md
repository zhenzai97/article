# Q026：post_es_mapping.json、seed_advertising_space.sql、seed_article_category.sql 是干什么的？

- **日期**：2026-07-16
- **状态**：已解答

---

## 问题描述

`sql/` 下这三个文件分别是干什么的？是不是表？

---

## 结论

**都不是 MySQL「建表」文件。**  
建表只在 `create_table.sql`。这三个分别是：**种子数据 ×2** + **Elasticsearch 索引映射 ×1**。

---

### 1. `seed_article_category.sql`

| 项 | 说明 |
|----|------|
| 类型 | **种子数据（INSERT）** |
| 作用对象 | MySQL 表 `article_category` |
| 内容 | 从旧 CMS 迁过来的文章分类（协会动态、网信要闻等） |
| 何时用 | 空库初始化分类，或重新灌入分类基础数据 |

```bash
mysql -uroot -p --default-character-set=utf8mb4 < sql/seed_article_category.sql
```

---

### 2. `seed_advertising_space.sql`

| 项 | 说明 |
|----|------|
| 类型 | **种子数据（INSERT）** |
| 作用对象 | MySQL 表 `advertising_space` |
| 内容 | 运营广告位（首页轮播 focus、宫格 channel 等） |
| 何时用 | 初始化广告位字典数据 |

```bash
mysql -uroot -p --default-character-set=utf8mb4 < sql/seed_advertising_space.sql
```

---

### 3. `post_es_mapping.json`

| 项 | 说明 |
|----|------|
| 类型 | **Elasticsearch 索引 mapping**（JSON，不是 SQL） |
| 作用对象 | ES 索引（模板项目里的「帖子 post」搜索） |
| 内容 | 定义 `title`、`content` 等字段类型与 IK 分词器 |
| 何时用 | 启用帖子 ES 搜索时，用该 JSON 创建索引（见根目录 README「Elasticsearch」章节） |

与当前业务里的 `article` 表无直接关系；来自 springboot-init 模板的帖子搜索能力，**未启用 ES 时可忽略**。

---

## 对比

| 文件 | 是不是建表 | 用途 |
|------|------------|------|
| `create_table.sql` | ✅ 是 | MySQL 表结构 DDL |
| `seed_article_category.sql` | ❌ | 往已有表插分类数据 |
| `seed_advertising_space.sql` | ❌ | 往已有表插广告位数据 |
| `post_es_mapping.json` | ❌ | ES 索引定义（可选） |

---

## 相关文件

- `sql/create_table.sql`
- `docs/project-conventions.md`
- 根目录 `README.md`（Elasticsearch 使用说明）
