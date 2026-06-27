# Q015：菜单表（menu）字段设计（对照后台新增菜单表单）

- **日期**：2026-06-27
- **状态**：已解答

---

## 问题描述

根据后台「新增菜单」表单设计菜单表，**去除**「是否外链」「关联项目」字段，给出菜单表完整字段。

---

## 表单字段与数据库映射

| 表单项 | 表字段 | 类型 | 必填 | 说明 |
|--------|--------|------|------|------|
| 上级菜单 | `parentId` | bigint | 是 | 父菜单 id；`0` = 主类目（根节点） |
| 菜单类型 | `menuType` | tinyint | 是 | `1` 目录、`2` 菜单、`3` 按钮 |
| 菜单图标 | `icon` | varchar(128) | 否 | 图标标识，如 `user`、`setting` |
| 菜单名称 | `menuName` | varchar(128) | **是** | 侧栏/表单展示名称 |
| 显示排序 | `sort` | int | **是** | 默认 `0`，越小越靠前 |
| 路由地址 | `path` | varchar(256) | 目录/菜单建议必填 | 前端路由，如 `/system/user` |
| 组件路径 | `component` | varchar(256) | 否 | 前端组件，如 `system/user/index` |
| 权限字符 | `permCode` | varchar(128) | 否 | 权限标识，如 `system:user:list` |
| 备注 | `remark` | varchar(512) | 否 | 备注说明 |
| 显示状态 | `visible` | tinyint | 是 | `1` 显示、`0` 隐藏（侧栏不展示，路由可保留） |
| 菜单状态 | `status` | tinyint | 是 | `1` 正常、`0` 停用 |
| 是否缓存 | `isCache` | tinyint | 是 | `1` 缓存、`0` 不缓存（对应前端 keep-alive） |

### 已去除（按需求不建字段）

| 表单项 | 说明 |
|--------|------|
| 是否外链 | 不建 `isExternalLink` 等字段 |
| 关联项目 | 不建 `projectId` / 关联项目字段 |

### 系统通用字段（与 role 表一致）

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | bigint PK | 主键 |
| `createUserId` | bigint | 创建人（可选） |
| `createTime` | datetime | 创建时间 |
| `updateTime` | datetime | 更新时间 |
| `isDelete` | tinyint | 逻辑删除 |

---

## 菜单表 `menu` 完整字段清单（共 16 个业务+审计字段）

| 序号 | 字段 | 类型 | 默认 | 说明 |
|------|------|------|------|------|
| 1 | `id` | bigint | 自增 | 主键 |
| 2 | `parentId` | bigint | 0 | 父菜单 id |
| 3 | `menuName` | varchar(128) | — | 菜单名称 |
| 4 | `menuType` | tinyint | — | 1目录 2菜单 3按钮 |
| 5 | `icon` | varchar(128) | null | 菜单图标 |
| 6 | `sort` | int | 0 | 显示排序 |
| 7 | `path` | varchar(256) | null | 路由地址 |
| 8 | `component` | varchar(256) | null | 组件路径 |
| 9 | `permCode` | varchar(128) | null | 权限字符 |
| 10 | `remark` | varchar(512) | null | 备注 |
| 11 | `visible` | tinyint | 1 | 显示状态 |
| 12 | `status` | tinyint | 1 | 菜单状态 |
| 13 | `isCache` | tinyint | 0 | 是否缓存 |
| 14 | `createUserId` | bigint | null | 创建人 |
| 15 | `createTime` | datetime | 当前时间 | 创建时间 |
| 16 | `updateTime` | datetime | 自动更新 | 更新时间 |
| 17 | `isDelete` | tinyint | 0 | 逻辑删除 |

索引：`idx_parentId(parentId)`

---

## 建表 SQL（已写入 `sql/create_table.sql`）

```sql
CREATE TABLE IF NOT EXISTS menu
(
    id           BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    parentId     BIGINT       DEFAULT 0 NOT NULL COMMENT '父菜单id，0表示主类目/根节点',
    menuName     VARCHAR(128) NOT NULL COMMENT '菜单名称',
    menuType     TINYINT      NOT NULL COMMENT '菜单类型：1-目录 2-菜单 3-按钮',
    icon         VARCHAR(128) NULL COMMENT '菜单图标',
    sort         INT          DEFAULT 0 NOT NULL COMMENT '显示排序',
    path         VARCHAR(256) NULL COMMENT '路由地址',
    component    VARCHAR(256) NULL COMMENT '组件路径',
    permCode     VARCHAR(128) NULL COMMENT '权限字符',
    remark       VARCHAR(512) NULL COMMENT '备注',
    visible      TINYINT      DEFAULT 1 NOT NULL COMMENT '显示状态：0-隐藏 1-显示',
    status       TINYINT      DEFAULT 1 NOT NULL COMMENT '菜单状态：0-停用 1-正常',
    isCache      TINYINT      DEFAULT 0 NOT NULL COMMENT '是否缓存：0-不缓存 1-缓存',
    createUserId BIGINT       NULL COMMENT '创建人id',
    createTime   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    isDelete     TINYINT      DEFAULT 0 NOT NULL,
    INDEX idx_parentId (parentId)
) COMMENT '菜单' COLLATE = utf8mb4_unicode_ci;
```

---

## 各菜单类型字段使用建议

| menuType | path | component | 侧栏 | 说明 |
|----------|------|-----------|------|------|
| 1 目录 | 建议有 | 通常为空 | 展示 | 只分组，如「系统管理」 |
| 2 菜单 | **必填** | **建议填** | 展示 | 可点击页面 |
| 3 按钮 | 可空 | 可空 | **不展示** | 仅 `permCode` 做按钮权限 |

查询侧栏树时：`menuType in (1,2)` 且 `visible=1` 且 `status=1`。

---

## 配套表（角色分配菜单，后续实现）

```sql
-- 角色菜单关联（超级管理员不写入，代码短路返回全部菜单）
CREATE TABLE role_menu (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    roleId     BIGINT NOT NULL,
    menuId     BIGINT NOT NULL,
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE KEY uk_role_menu (roleId, menuId)
) COMMENT '角色菜单关联';
```

---

## 相关文档

- [Q012 菜单权限与超管](./Q012-role-menu-super-admin.md)
- [Q013 角色表字段](./Q013-role-table-all-fields.md)
