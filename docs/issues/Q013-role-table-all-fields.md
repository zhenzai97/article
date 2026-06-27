# Q013：角色表（role）完整字段清单

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

给出角色表的所有字段定义。

---

## 角色表 `role` 完整字段

| 序号 | 字段名 | 类型 | 空 | 默认值 | 说明 |
|------|--------|------|-----|--------|------|
| 1 | `id` | bigint | NOT NULL | 自增 | 主键 |
| 2 | `roleCode` | varchar(64) | NOT NULL | — | 角色编码，程序/JWT 使用，唯一，如 `admin`、`editor`、`user`、`ban` |
| 3 | `roleName` | varchar(128) | NOT NULL | — | 角色名称，界面展示，如「超级管理员」「内容编辑」 |
| 4 | `roleDesc` | varchar(512) | NULL | — | 角色描述 |
| 5 | `status` | tinyint | NOT NULL | 1 | 状态：`0` 禁用，`1` 启用 |
| 6 | `sort` | int | NOT NULL | 0 | 排序号，越小越靠前 |
| 7 | `isBuiltin` | tinyint | NOT NULL | 0 | 是否内置：`0` 否，`1` 是（内置角色不可删除） |
| 8 | `isSuperAdmin` | tinyint | NOT NULL | 0 | 是否超级管理员：`1` 拥有全部菜单，不参与 `role_menu` 分配 |
| 9 | `dataScope` | tinyint | NULL | 1 | 数据范围（可选）：`1` 全部 `2` 本部门 `3` 仅本人 |
| 10 | `createUserId` | bigint | NULL | — | 创建人用户 id（审计，可选） |
| 11 | `createTime` | datetime | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| 12 | `updateTime` | datetime | NOT NULL | ON UPDATE | 更新时间 |
| 13 | `isDelete` | tinyint | NOT NULL | 0 | 逻辑删除：`0` 未删，`1` 已删 |

### 索引

| 索引名 | 类型 | 字段 |
|--------|------|------|
| PRIMARY | 主键 | `id` |
| `uk_roleCode` | 唯一 | `roleCode` |

---

## 建表 SQL

```sql
CREATE TABLE IF NOT EXISTS role
(
    id           BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    roleCode     VARCHAR(64)  NOT NULL COMMENT '角色编码（程序用，如 admin/editor/user/ban）',
    roleName     VARCHAR(128) NOT NULL COMMENT '角色名称（展示用）',
    roleDesc     VARCHAR(512) NULL COMMENT '角色描述',
    status       TINYINT      DEFAULT 1 NOT NULL COMMENT '状态：0-禁用 1-启用',
    sort         INT          DEFAULT 0 NOT NULL COMMENT '排序',
    isBuiltin    TINYINT      DEFAULT 0 NOT NULL COMMENT '是否内置：0-否 1-是（不可删除）',
    isSuperAdmin TINYINT      DEFAULT 0 NOT NULL COMMENT '是否超级管理员：1-拥有全部菜单',
    dataScope    TINYINT      DEFAULT 1 NULL COMMENT '数据范围：1-全部 2-本部门 3-仅本人',
    createUserId BIGINT       NULL COMMENT '创建人id',
    createTime   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updateTime   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete     TINYINT      DEFAULT 0 NOT NULL COMMENT '是否删除：0-否 1-是',
    UNIQUE KEY uk_roleCode (roleCode)
) COMMENT '角色' COLLATE = utf8mb4_unicode_ci;
```

---

## 字段分组说明

### 核心（必须有）

`id`、`roleCode`、`roleName`、`status`、`createTime`、`updateTime`、`isDelete`

### 业务（强烈建议）

`roleDesc`、`sort`、`isBuiltin`、`isSuperAdmin`

### 扩展（按需）

`dataScope`、`createUserId` — 后台数据权限、操作审计时再启用

### 不在角色表内的数据

| 内容 | 应放位置 |
|------|----------|
| 菜单 id 列表 | `role_menu` |
| 用户 id 列表 | `user_role` |
| 接口/按钮权限 | `permission` + `role_permission` |

---

## 初始数据示例

```sql
INSERT INTO role (roleCode, roleName, roleDesc, isBuiltin, isSuperAdmin, sort) VALUES
('admin',  '超级管理员', '拥有全部菜单，无需分配', 1, 1, 10),
('editor', '内容编辑',   '管理帖子等内容', 0, 0, 20),
('user',   '普通用户',   '默认角色', 1, 0, 30),
('ban',    '被封号',     '禁止登录', 1, 0, 40);
```

---

## 相关文档

- [Q011 角色表设计](./Q011-role-table-design.md)
- [Q012 菜单权限与超管](./Q012-role-menu-super-admin.md)
