# Q001：熟悉本项目整体结构与功能

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

请熟悉这个项目（参考 `项目功能详细介绍文档.md`）。

---

## 结论摘要

本项目 **springboot-init** 是基于程序员鱼皮模板的 Spring Boot 2.7.2 后端 API，业务模型为「内容社区」。

### 技术栈

- Spring MVC、AOP、MyBatis Plus、Session 登录（改造中：JWT）
- MySQL、Redis（可选）、Elasticsearch（可选）、腾讯云 COS、微信集成
- Knife4j 接口文档

### 六大业务模块

| 模块 | 路由 | 能力 |
|------|------|------|
| 用户 | `/user` | 注册/登录/注销、微信 OAuth、角色权限 |
| 帖子 | `/post` | CRUD、MySQL 检索、ES 全文搜索 |
| 点赞 | `/post_thumb` | Toggle 点赞 |
| 收藏 | `/post_favour` | Toggle 收藏、收藏列表 |
| 文件 | `/file` | COS 上传 |
| 微信 | `/` | 公众号验证与消息 |

### 分层架构

`Controller → Service → Mapper/EsDao`，横切：`AuthInterceptor`、`LogInterceptor`、`GlobalExceptionHandler`。

### 启动信息

- 端口：`8101`，上下文：`/api`
- 接口文档：`http://localhost:8101/api/doc.html`
- 最小启动：MySQL + 执行 `sql/create_table.sql` + 运行 `MainApplication`

---

## 相关文档

- [项目功能详细介绍文档.md](../../项目功能详细介绍文档.md)
- [项目学习路线.md](../../项目学习路线.md)
