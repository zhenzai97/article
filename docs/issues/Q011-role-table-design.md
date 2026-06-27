# Q011：角色表（role）字段设计建议

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

需要设计角色表，角色表应该有哪些字段？

---

## 现状

当前项目 **未单独建角色表**，角色写在用户表上：

- `user.userRole`：`user` / `admin` / `ban`（字符串）
- `UserRoleEnum` 硬编码三种角色
- `@AuthCheck(mustRole = ADMIN)` + JWT 载荷里的 `userRole` 直接比对字符串

适合模板学习，但不利于：动态增删角色、一人多角色、细粒度权限。

---

## 设计目标（建议先想清楚）

| 目标 | 表结构复杂度 |
|------|--------------|
| 仅把角色从用户表拆出来，仍可动态配置角色名 | **角色表 + 改 user 外键** |
| 一个用户多个角色 | **+ 用户角色关联表** |
| 按钮/接口级权限（RBAC） | **+ 权限表 + 角色权限关联表** |

对本「内容社区」项目，**推荐二期目标**：角色表 + 用户角色关联表；权限表可后续再加。

---

## 一、角色表 `role`（核心字段）

### 必选字段

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | bigint PK | 主键，建议与项目一致用雪花或自增 |
| `roleCode` | varchar(64) UNIQUE | **角色编码**，程序与 JWT 使用，如 `user`、`admin`、`ban`；稳定不变 |
| `roleName` | varchar(128) | **角色名称**，展示用，如「普通用户」「管理员」 |
| `status` | tinyint | 状态：`0` 禁用 / `1` 启用；禁用后不可分配给新用户 |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |
| `isDelete` | tinyint | 逻辑删除（与项目全局约定一致） |

### 强烈建议

| 字段 | 类型 | 说明 |
|------|------|------|
| `roleDesc` | varchar(512) | 角色说明 |
| `sort` | int | 列表排序，越小越靠前 |
| `isBuiltin` | tinyint | 是否内置角色：`1` 不可删（如 admin、ban） |

### 可选（按需）

| 字段 | 类型 | 说明 |
|------|------|------|
| `dataScope` | tinyint | 数据范围：本人 / 本部门 / 全部（后台扩展用） |
| `createUserId` | bigint | 创建人（审计） |

### 不建议放进角色表

| 内容 | 原因 |
|------|------|
| 具体权限列表 | 应放 `permission` + `role_permission` |
| 用户 id 列表 | 应放 `user_role` 关联表 |

---

## 二、建表 SQL 示例

```sql
CREATE TABLE IF NOT EXISTS role
(
    id          BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    roleCode    VARCHAR(64)  NOT NULL COMMENT '角色编码（程序用，如 user/admin/ban）',
    roleName    VARCHAR(128) NOT NULL COMMENT '角色名称（展示用）',
    roleDesc    VARCHAR(512) NULL COMMENT '角色描述',
    status      TINYINT      DEFAULT 1 NOT NULL COMMENT '状态：0-禁用 1-启用',
    sort        INT          DEFAULT 0 NOT NULL COMMENT '排序',
    isBuiltin   TINYINT      DEFAULT 0 NOT NULL COMMENT '是否内置：0-否 1-是（内置不可删）',
    createTime  DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime  DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete    TINYINT      DEFAULT 0 NOT NULL COMMENT '是否删除',
    UNIQUE KEY uk_roleCode (roleCode)
) COMMENT '角色' COLLATE = utf8mb4_unicode_ci;
```

### 初始数据（对齐现有枚举）

```sql
INSERT INTO role (roleCode, roleName, roleDesc, isBuiltin, sort) VALUES
('user',  '普通用户', '默认角色', 1, 10),
('admin', '管理员',   '拥有后台管理权限', 1, 20),
('ban',   '被封号',   '禁止登录与操作', 1, 30);
```

---

## 三、配套表（推荐一并规划）

### 1. 用户角色关联表 `user_role`（支持多角色）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | bigint PK | 主键 |
| `userId` | bigint | 用户 id |
| `roleId` | bigint | 角色 id |
| `createTime` | datetime | 分配时间 |

索引：`uk_userId_roleId(userId, roleId)`、`idx_roleId(roleId)`

用户表改造：

- **方案 A**：删除 `user.userRole`，只通过 `user_role` 关联
- **方案 B（过渡）**：保留 `user.userRole` 作「主角色」，JWT 仍写主角色，逐步迁移

### 2. 权限表 `permission`（若要做接口/按钮权限）

| 字段 | 说明 |
|------|------|
| `id` | 主键 |
| `permCode` | 权限编码，如 `user:add`、`post:delete` |
| `permName` | 权限名称 |
| `permType` | 类型：菜单 / 按钮 / 接口 |
| `parentId` | 父权限（树形菜单） |
| `path` / `apiPath` | 前端路由或后端 API |
| `status`、`sort`、`createTime`、`updateTime`、`isDelete` | 常规字段 |

### 3. 角色权限关联表 `role_permission`

| 字段 | 说明 |
|------|------|
| `id` | 主键 |
| `roleId` | 角色 id |
| `permissionId` | 权限 id |

---

## 四、与现有 JWT / AuthCheck 的衔接

### 当前

```java
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 比对字符串 "admin"
JWT payload: userRole = "admin"
```

### 引入角色表后

| 项 | 建议 |
|----|------|
| JWT 载荷 | 仍放 `roleCode` 列表或主角色 `roleCode`（不要只放 roleId，可读性差） |
| `AuthInterceptor` | `mustRole` 仍比对 `roleCode`，或改为 `mustPerm` 查 `role_permission` |
| `getLoginUser` | 查库时关联 `user_role` + `role`，校验 `ban`、是否禁用 |
| `UserRoleEnum` | 可保留作常量，或改为从 DB 读角色列表 |

---

## 五、字段设计原则小结

1. **`roleCode` 与 `roleName` 分离**：编码给程序/JWT，名称给界面，改显示名不动代码。
2. **`ban` 建议仍是角色或独立 `user.status`**：封号是「状态」还是「角色」要统一；当前项目把 `ban` 当角色，可继续沿用并在 `isBuiltin=1` 标记。
3. **逻辑删除 + 内置标记**：防止删掉 `admin` 导致系统不可用。
4. **不要在大表上堆 JSON 权限**：权限独立表，方便 `@AuthCheck` 演进为权限点校验。
5. **与项目风格一致**：`createTime`、`updateTime`、`isDelete` 与 `user`、`post` 表保持一致。

---

## 六、实施路线建议

| 阶段 | 内容 |
|------|------|
| 1 | 建 `role` 表 + 初始化三条数据 |
| 2 | 建 `user_role`，迁移 `user.userRole` 数据 |
| 3 | 改 `UserServiceImpl` / JWT / `AuthInterceptor` 读关联表 |
| 4（可选） | `permission` + `role_permission` + 改造 `@AuthCheck` |

---

## 相关文件（当前）

- `user.userRole` — `User.java`、`create_table.sql`
- `UserRoleEnum.java` — 角色枚举
- `AuthInterceptor.java` — 角色校验
- JWT `userRole` 载荷 — `JwtUtils.java`
