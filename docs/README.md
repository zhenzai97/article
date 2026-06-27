# 项目文档目录

本目录存放 `springboot-init` 项目的设计、改造、运维等相关文档。

## 文档索引

| 文档 | 说明 |
|------|------|
| [项目问题集](./project-issues.md) | 问题清单索引，每条问题对应 `issues/` 下详细文档 |
| [Session 改造 JWT 登录认证方案](./session-to-jwt-migration.md) | 将 Session 登录态改为 JWT Token 的完整改造指南 |

## 约定

- 新增文档请使用 Markdown 格式，文件名使用英文小写 + 连字符（如 `feature-xxx.md`）
- 文档内涉及路径、接口、配置项须与源码保持一致，改造后及时更新
- **问题收录**：每提出一个新问题，在 [project-issues.md](./project-issues.md) 追加一行，并新建 `issues/Qxxx-简短英文名.md`
