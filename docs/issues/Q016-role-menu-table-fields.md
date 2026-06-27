# Q016：角色菜单关联表（role_menu）字段设计

- **日期**：2026-06-27
- **状态**：已解答

---

## 问题描述

角色与菜单需要关联，生成角色菜单表字段。

---

## 表名

`role_menu`（角色 ↔ 菜单，多对多中间表）

```
role ──N:M── role_menu ──N:M── menu
```

---

## 完整字段清单

| 序号 | 字段 | 类型 | 空 | 默认值 | 说明 |
|------|------|------|-----|--------|------|
| 1 | `id` | bigint | NOT NULL | 自增 | 主键 |
| 2 | `roleId` | bigint | NOT NULL | — | 角色 id，关联 `role.id` |
| 3 | `menuId` | bigint | NOT NULL | — | 菜单 id，关联 `menu.id` |
| 4 | `createUserId` | bigint | NULL | — | 分配操作人 id（可选，审计用） |
| 5 | `createTime` | datetime | NOT NULL | CURRENT_TIMESTAMP | 分配时间 |

### 索引

| 索引名 | 类型 | 字段 | 说明 |
|--------|------|------|------|
| PRIMARY | 主键 | `id` | — |
| `uk_role_menu` | 唯一 | `(roleId, menuId)` | 同一角色不能重复绑定同一菜单 |
| `idx_roleId` | 普通 | `roleId` | 按角色查菜单 |
| `idx_menuId` | 普通 | `menuId` | 按菜单查角色（较少用） |

---

## 建表 SQL（已写入 `sql/create_table.sql`）

```sql
CREATE TABLE IF NOT EXISTS role_menu
(
    id           BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    roleId       BIGINT NOT NULL COMMENT '角色 id',
    menuId       BIGINT NOT NULL COMMENT '菜单 id',
    createUserId BIGINT NULL COMMENT '分配人 id',
    createTime   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '分配时间',
    UNIQUE KEY uk_role_menu (roleId, menuId),
    INDEX idx_roleId (roleId),
    INDEX idx_menuId (menuId)
) COMMENT '角色菜单关联';
```

---

## 设计说明

### 1. 硬删除，无 `isDelete`

与 `post_thumb`、`post_favour` 一致，关联表采用 **硬删除**：

- 取消某菜单权限：直接 `DELETE FROM role_menu WHERE roleId=? AND menuId=?`
- 重新分配：可先 `DELETE WHERE roleId=?` 再批量 `INSERT`

关联表行数小、无独立业务含义，一般不做逻辑删除。

### 2. 无 `updateTime`

权限分配以「覆盖式」更新为主（删掉旧关联、插入新关联），通常只记录 **何时分配**，不记录修改时间。

### 3. 超级管理员不写此表

`role.isSuperAdmin = 1`（如 `admin`）的角色 **不要** 插入 `role_menu`。

查询用户菜单时：超管 → 返回全部 `menu`；普通角色 → `JOIN role_menu`。

### 4. 分配菜单时的业务规则

| 规则 | 说明 |
|------|------|
| 超管角色 | 拒绝调用分配接口，或提示「超管拥有全部菜单」 |
| 父菜单 | 勾选子菜单时，建议同时勾选父级目录，避免侧栏断层 |
| 按钮权限 | `menuType=3` 的按钮也可写入，用于页面内按钮权限 |
| 角色/菜单停用 | 查询时过滤 `role.status=1`、`menu.status=1` |

---

## 典型接口（后续实现参考）

| 接口 | 说明 |
|------|------|
| `GET /role/menuIds?roleId=` | 查询某角色已分配的 menuId 列表 |
| `POST /role/assignMenu` | 入参 `roleId` + `menuIds[]`，覆盖保存 |
| `GET /menu/list/my/tree` | 当前登录用户可见菜单树（多角色取并集） |

### 分配菜单伪逻辑

```java
// 1. 校验非超管角色
// 2. DELETE FROM role_menu WHERE roleId = ?
// 3. BATCH INSERT (roleId, menuId, createUserId)
```

### 查询角色菜单 SQL 思路

```sql
SELECT m.* FROM menu m
INNER JOIN role_menu rm ON m.id = rm.menuId
WHERE rm.roleId IN (用户角色id列表)
  AND m.status = 1 AND m.isDelete = 0
```

---

## 配套表（用户 ↔ 角色，若尚未建）

| 表 | 作用 |
|----|------|
| `user_role` | 用户拥有哪些角色，多角色菜单取 **并集** |

---

## 相关文档

- [Q012 菜单权限与超管](./Q012-role-menu-super-admin.md)
- [Q015 菜单表字段](./Q015-menu-table-fields.md)
- [Q013 角色表字段](./Q013-role-table-all-fields.md)
