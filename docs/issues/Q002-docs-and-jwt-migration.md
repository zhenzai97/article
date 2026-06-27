# Q002：创建 docs 目录并生成 Session 改造 JWT 文档

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

1. 在项目根目录生成 `docs` 文件夹，存放后续生成的文档。
2. 当前项目使用 Session 控制登录，需要改为 Token / JWT，生成改造文档。

---

## 处理结果

### 1. 已创建目录结构

```
docs/
├── README.md
├── project-issues.md          # 本问题集（Q004 后新增）
├── session-to-jwt-migration.md  # JWT 改造主文档
└── issues/                    # 各问题详细附件
```

### 2. JWT 改造文档要点

主文档：[session-to-jwt-migration.md](../session-to-jwt-migration.md)

| 章节 | 内容 |
|------|------|
| 现状分析 | Session 写入点、`getLoginUser` 调用链 |
| 目标设计 | Bearer Token、Claims（userId + userRole） |
| 改造步骤 | 依赖配置 → JwtUtils → UserServiceImpl → 跨域/前端 |
| 注销策略 | 客户端删除 / Redis 黑名单 |
| Checklist | 新增与修改文件清单 |
| 测试计划 | 单元测试与接口回归 |

**核心思路**：保持 `getLoginUser(HttpServletRequest)` 签名，内部从 Session 改为解析 JWT，减少 Controller 改动。

---

## 相关文件

- [docs/session-to-jwt-migration.md](../session-to-jwt-migration.md)
- `UserServiceImpl.java`（待按文档实施改造）
- `JwtUtils.java`（已创建）
