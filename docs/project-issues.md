# 项目问题集

> 记录本项目开发、学习过程中提出的问题及解答。  
> 每个问题对应一份详细文档，见 `issues/` 目录。

**维护约定**：

- 自 2026-06-26 起，每新增一个问题，在本表追加一行，并在 `issues/` 下新增对应详细文档。
- **自 2026-07-15 起**：对话中提出的每个问题，均按本约定即时收录（表中追加一行 + `issues/Qxxx-*.md` 详细文档）。

---

## 问题清单

| ID | 日期 | 问题摘要 | 状态 | 详细文档 |
|----|------|----------|------|----------|
| Q001 | 2026-06-26 | 熟悉本项目整体结构与功能 | 已解答 | [Q001-project-overview.md](./issues/Q001-project-overview.md) |
| Q002 | 2026-06-26 | 创建 docs 目录并生成 Session 改造 JWT 文档 | 已解答 | [Q002-docs-and-jwt-migration.md](./issues/Q002-docs-and-jwt-migration.md) |
| Q003 | 2026-06-26 | 为 JwtUtils.java 方法生成注释 | 已解答 | [Q003-jwtutils-comments.md](./issues/Q003-jwtutils-comments.md) |
| Q004 | 2026-06-26 | application.yml 中 jwt.secret 提示「无法解析」的原因 | 已解答 | [Q004-jwt-secret-cannot-resolve.md](./issues/Q004-jwt-secret-cannot-resolve.md) |
| Q005 | 2026-06-26 | JwtUtils.generateToken 如何读取 application.yml 中的 jwt 配置 | 已解答 | [Q005-read-jwt-config-from-yml.md](./issues/Q005-read-jwt-config-from-yml.md) |
| Q006 | 2026-06-26 | API 返回时间格式改为 yyyy-MM-dd HH:mm:ss | 已解答 | [Q006-date-time-format-in-api-response.md](./issues/Q006-date-time-format-in-api-response.md) |
| Q007 | 2026-06-26 | 登录返回 Token+用户信息 vs 分 login/getInfo 哪种更合理 | 已解答 | [Q007-login-response-token-vs-getinfo.md](./issues/Q007-login-response-token-vs-getinfo.md) |
| Q008 | 2026-06-26 | getLoginUser 如何从请求中取 Token | 已解答 | [Q008-get-token-in-getloginuser.md](./issues/Q008-get-token-in-getloginuser.md) |
| Q009 | 2026-06-26 | @AuthCheck 接口（如 addUser）如何校验 Token | 已解答 | [Q009-authcheck-token-validation.md](./issues/Q009-authcheck-token-validation.md) |
| Q010 | 2026-06-26 | DELETE /user/delete/12 如何取路径动态参数 id | 已解答 | [Q010-path-variable-delete-user.md](./issues/Q010-path-variable-delete-user.md) |
| Q011 | 2026-06-26 | 角色表（role）应有哪些字段、如何与现有权限衔接 | 已解答 | [Q011-role-table-design.md](./issues/Q011-role-table-design.md) |
| Q012 | 2026-06-26 | 多角色菜单权限 + 超级管理员 admin 自动拥有全部菜单 | 已解答 | [Q012-role-menu-super-admin.md](./issues/Q012-role-menu-super-admin.md) |
| Q013 | 2026-06-26 | 角色表（role）完整字段清单 | 已解答 | [Q013-role-table-all-fields.md](./issues/Q013-role-table-all-fields.md) |
| Q014 | 2026-06-27 | RoleController 提示文件以错误编码 UTF-8 加载（乱码） | 已解答 | [Q014-file-encoding-utf8-garbled.md](./issues/Q014-file-encoding-utf8-garbled.md) |
| Q015 | 2026-06-27 | 菜单表（menu）字段设计（去除外链、关联项目） | 已解答 | [Q015-menu-table-fields.md](./issues/Q015-menu-table-fields.md) |
| Q016 | 2026-06-27 | 角色菜单关联表（role_menu）字段设计 | 已解答 | [Q016-role-menu-table-fields.md](./issues/Q016-role-menu-table-fields.md) |
| Q017 | 2026-06-27 | MenuService.getMenuVOPage 静态调用报错 | 已解答 | [Q017-menuservice-static-call-error.md](./issues/Q017-menuservice-static-call-error.md) |
| Q018 | 2026-06-27 | MenuServiceImpl.getMenuVO 的 @Override 提示未重写 | 已解答 | [Q018-override-getmenuvo-not-in-interface.md](./issues/Q018-override-getmenuvo-not-in-interface.md) |
| Q019 | 2026-06-27 | 菜单列表应返回树形还是分页扁平数据 | 已解答 | [Q019-menu-tree-vs-page.md](./issues/Q019-menu-tree-vs-page.md) |
| Q020 | 2026-06-27 | listMenuTreeVO 获取菜单树接口详细说明 | 已解答 | [Q020-list-menu-tree-vo-explained.md](./issues/Q020-list-menu-tree-vo-explained.md) |
| Q021 | 2026-07-15 | @TableName(value = "article_category") 注解的作用 | 已解答 | [Q021-tablename-annotation.md](./issues/Q021-tablename-annotation.md) |
| Q022 | 2026-07-15 | @Data 注解的作用（Lombok） | 已解答 | [Q022-lombok-data-annotation.md](./issues/Q022-lombok-data-annotation.md) |

---

## 统计

- 问题总数：22
- 已解答：22
- 待处理：0

---

## 新增问题模板

复制以下内容追加到上表，并创建 `issues/Qxxx-简短英文名.md`：

```markdown
| Qxxx | YYYY-MM-DD | 问题摘要 | 待处理 | [Qxxx-xxx.md](./issues/Qxxx-xxx.md) |
```

详细文档建议结构：

1. 问题描述（原文）
2. 原因分析
3. 解决方案 / 结论
4. 相关文件与参考链接
