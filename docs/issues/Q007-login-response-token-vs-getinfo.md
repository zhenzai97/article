# Q007：登录接口应返回 Token+用户信息，还是分两次请求？

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

1. **现状**：登录接口直接返回 `token` + 用户部分信息（`LoginUserVO`）。
2. **以往项目**：`/api/login` 只返回 `token`，再调 `/api/getInfo` 获取用户信息。

哪种方式更合理？

---

## 结论（建议）

**两种都合理，没有绝对对错**；对本项目这类内容社区后端，更推荐 **折中方案**：

| 接口 | 返回内容 |
|------|----------|
| `POST /user/login` | `token` + **少量核心字段**（id、userName、userAvatar、userRole） |
| `GET /user/get/login` | 完整/最新的用户信息（作「我是谁」、刷新恢复登录态） |

即：**登录时带一份「快照」方便前端立刻渲染，同时保留独立的 getInfo 接口**。

---

## 两种方案对比

### 方案 A：登录一次返回 Token + 用户信息（当前做法）

```json
{
  "token": "...",
  "id": "...",
  "userName": "...",
  "userAvatar": "...",
  "userRole": "user"
}
```

| 优点 | 缺点 |
|------|------|
| 少一次请求，登录后立刻能展示头像/昵称 | 登录响应体变大 |
| 弱网、移动端体验更好 | 认证与资料查询职责略混合 |
| 鱼皮模板、不少 OAuth 实践采用类似方式 | 用户改资料后，登录返回的仍是旧快照 |

**适合**：中小型项目、管理后台、社区前台，用户信息字段不多。

### 方案 B：登录只返回 Token，再调 getInfo（以往项目）

```json
// POST /login → { "token": "..." }
// GET /getInfo → { "id", "userName", ... }
```

| 优点 | 缺点 |
|------|------|
| 职责清晰：登录=发证，getInfo=查资料 | 登录流程至少 2 次请求 |
| 登录接口稳定、体积极小 | 前端要多写一步，失败时要处理「有 token 但拿不到用户信息」 |
| getInfo 可复用：刷新页面、Token 仍有效时恢复用户态 | 首屏略慢 |

**适合**：用户资料复杂、多端字段差异大、严格分层的大型后端。

---

## 为什么推荐「折中」？

1. **登录返回核心字段**：导航栏、头像、权限判断（是否 admin）不依赖第二次请求，体验好。
2. **保留 `GET /user/get/login`**：
   - 用户刷新页面：本地有 token → 调 getInfo 恢复用户态（不必重新登录）
   - 用户修改资料后：重新 getInfo 拿最新数据
   - Token 续期后：统一用 getInfo 拉当前用户

本项目 **已有** `GET /user/get/login`，与折中方案天然匹配。

---

## 对本项目的具体建议

### 登录 `POST /user/login` 建议返回

```json
{
  "token": "eyJ...",
  "id": "2070350870232080386",
  "userName": "xxx",
  "userAvatar": "https://...",
  "userRole": "user"
}
```

`createTime`、`updateTime` 登录时 **可不返回**（登录页一般用不到，减少噪音）。

### getInfo `GET /user/get/login` 建议返回

与登录相同或更完整的 `LoginUserVO`（**不含 token**，或 token 可选不返回）。

### 前端典型流程

```
登录成功 → 存 token → 用登录返回的核心字段渲染 UI
页面刷新 → 读 token → GET /user/get/login → 恢复用户态
改资料后 → PUT /user/update/my → 再 GET /user/get/login
```

---

## 何时选「只返回 Token」？

- 登录响应必须极简（安全审计、网关规范）
- 用户信息字段很多或按端差异大
- 团队规范强制「认证接口不返回业务数据」

若选纯 Token 方案，务必保证 getInfo **稳定、快速、与 Token 鉴权一致**，否则前端登录体验会差。

---

## 相关接口（本项目）

| 接口 | 当前角色 |
|------|----------|
| `POST /user/login` | 登录，当前返回 token + LoginUserVO |
| `GET /user/get/login` | 获取当前登录用户，等价于 getInfo |

---

## 参考

- REST 常见实践：登录/me 分离 vs 合并均有广泛使用
- 鱼皮 springboot-init 模板：登录直接返回 `LoginUserVO`（偏方案 A）
