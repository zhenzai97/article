# sql 目录说明

按 [项目开发约定](../docs/project-conventions.md) 组织：

| 文件 | 用途 |
|------|------|
| `create_table.sql` | **唯一**建表 / DDL（新建表、改字段只追加这里） |
| `seed_*.sql` | 种子数据（INSERT），与建表分离 |
| `post_es_mapping.json` | Elasticsearch 索引映射（非 MySQL） |

## 当前文件

```
sql/
├── create_table.sql           # 全部表结构
├── seed_article_category.sql  # 文章分类
├── seed_advertising_space.sql # 运营广告位
├── seed_home_section.sql      # 首页区块默认配置
├── seed_tourism_content.sql   # 文旅内容
└── post_es_mapping.json       # 帖子 ES mapping（可选）
```

## 禁止

- 不要新增 `create_xxx_table.sql` 拆分建表文件
