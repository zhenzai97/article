# Q012：多角色菜单权限 + 超级管理员自动拥有全部菜单

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

1. 需要根据角色分配不同菜单显示，角色不止 `user` / `admin` / `ban` 三种，会有更多自定义角色。
2. **超级管理员 `admin`** 应自动看到所有菜单，**不需要**在后台手动给 admin 勾选菜单。

---

## 设计结论

采用 **RBAC 菜单模型**：

```
用户 user ──N:M── user_role ──N:M── role ──N:M── role_menu ──N:M── menu
```

**超级管理员规则（核心）**：在 **查询菜单的业务逻辑里** 判断——若当前用户拥有 `roleCode = admin`（或角色表 `isSuperAdmin = 1`），**直接返回全部启用菜单树**，**不查 `role_menu`**。

这样新增菜单时，admin 自动可见，无需重新分配。

---

## 一、表结构设计

### 1. 角色表 `role`（在 Q011 基础上扩展）

| 字段 | 说明 |
|------|------|
| `id` | 主键 |
| `roleCode` | 编码，如 `admin`、`editor`、`user`、`ban` |
| `roleName` | 展示名 |
| `roleDesc` | 描述 |
| `status` | 0 禁用 / 1 启用 |
| `sort` | 排序 |
| `isBuiltin` | 内置角色不可删 |
| **`isSuperAdmin`** | **1 = 超级管理员，拥有全部菜单，不参与 role_menu 分配** |
| `createTime` / `updateTime` / `isDelete` | 常规 |

```sql
-- admin 标记为超级管理员
UPDATE role SET isSuperAdmin = 1 WHERE roleCode = 'admin';
```

也可用 **不写 `isSuperAdmin` 字段**，代码里固定 `roleCode.equals("admin")` 判断；字段方式更灵活（以后可有多个超管角色）。

### 2. 菜单表 `menu`

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | bigint PK | 主键 |
| `parentId` | bigint | 父菜单 id，`0` 为根 |
| `menuName` | varchar(128) | 菜单名称 |
| `menuType` | tinyint | `1` 目录 `2` 菜单 `3` 按钮（前端侧栏一般只用目录+菜单） |
| `path` | varchar(256) | 前端路由，如 `/user/list` |
| `component` | varchar(256) | 前端组件路径，如 `user/UserList` |
| `icon` | varchar(128) | 图标 |
| `sort` | int | 排序 |
| `status` | tinyint | 0 禁用 / 1 启用 |
| `visible` | tinyint | 0 隐藏 / 1 显示（路由仍存在但侧栏不展示） |
| `permCode` | varchar(128) | 权限标识，如 `user:list`，可与接口权限统一 |
| `createTime` / `updateTime` / `isDelete` | | 常规 |

索引：`idx_parentId(parentId)`

### 3. 用户角色关联 `user_role`

| 字段 | 说明 |
|------|------|
| `userId` | 用户 id |
| `roleId` | 角色 id |

唯一：`(userId, roleId)`

### 4. 角色菜单关联 `role_menu`（普通角色用）

| 字段 | 说明 |
|------|------|
| `roleId` | 角色 id |
| `menuId` | 菜单 id |

唯一：`(roleId, menuId)`

**注意**：**不要**给 `admin` 插入 `role_menu` 记录；超管走代码短路。

---

## 二、建表 SQL 示例

```sql
-- 角色表
CREATE TABLE IF NOT EXISTS role (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    roleCode     VARCHAR(64)  NOT NULL COMMENT '角色编码',
    roleName     VARCHAR(128) NOT NULL COMMENT '角色名称',
    roleDesc     VARCHAR(512) NULL,
    status       TINYINT DEFAULT 1 NOT NULL COMMENT '0禁用 1启用',
    sort         INT DEFAULT 0 NOT NULL,
    isBuiltin    TINYINT DEFAULT 0 NOT NULL COMMENT '内置不可删',
    isSuperAdmin TINYINT DEFAULT 0 NOT NULL COMMENT '1超级管理员拥有全部菜单',
    createTime   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    isDelete     TINYINT DEFAULT 0 NOT NULL,
    UNIQUE KEY uk_roleCode (roleCode)
) COMMENT '角色';

-- 菜单表
CREATE TABLE IF NOT EXISTS menu (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    parentId   BIGINT DEFAULT 0 NOT NULL COMMENT '父菜单id，0为根',
    menuName   VARCHAR(128) NOT NULL COMMENT '菜单名称',
    menuType   TINYINT NOT NULL COMMENT '1目录 2菜单 3按钮',
    path       VARCHAR(256) NULL COMMENT '路由路径',
    component  VARCHAR(256) NULL COMMENT '组件路径',
    icon       VARCHAR(128) NULL,
    sort       INT DEFAULT 0 NOT NULL,
    status     TINYINT DEFAULT 1 NOT NULL,
    visible    TINYINT DEFAULT 1 NOT NULL COMMENT '侧栏是否显示',
    permCode   VARCHAR(128) NULL COMMENT '权限标识',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    isDelete   TINYINT DEFAULT 0 NOT NULL,
    INDEX idx_parentId (parentId)
) COMMENT '菜单';

-- 用户角色
CREATE TABLE IF NOT EXISTS user_role (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId     BIGINT NOT NULL,
    roleId     BIGINT NOT NULL,
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE KEY uk_user_role (userId, roleId),
    INDEX idx_roleId (roleId)
) COMMENT '用户角色关联';

-- 角色菜单（普通角色）
CREATE TABLE IF NOT EXISTS role_menu (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    roleId     BIGINT NOT NULL,
    menuId     BIGINT NOT NULL,
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE KEY uk_role_menu (roleId, menuId),
    INDEX idx_menuId (menuId)
) COMMENT '角色菜单关联';
```

### 初始角色示例

```sql
INSERT INTO role (roleCode, roleName, roleDesc, isBuiltin, isSuperAdmin, sort) VALUES
('admin',   '超级管理员', '拥有全部菜单，无需分配', 1, 1, 10),
('editor',  '内容编辑',   '可管理帖子等内容', 0, 0, 20),
('user',    '普通用户',   '前台基础权限', 1, 0, 30),
('ban',     '被封号',     '禁止登录', 1, 0, 40);
```

---

## 三、核心业务逻辑：获取当前用户菜单树

### 接口建议

```
GET /api/menu/list/my/tree
Authorization: Bearer <token>
```

返回前端侧栏用的 **树形 JSON**。

### 伪代码（关键）

```java
public List<MenuVO> getMyMenuTree(User loginUser) {
    List<Role> roles = getRolesByUserId(loginUser.getId());

    // 1. 超级管理员：返回全部启用菜单，不查 role_menu
    boolean isSuperAdmin = roles.stream()
            .anyMatch(r -> r.getIsSuperAdmin() == 1
                || "admin".equals(r.getRoleCode()));  // 可二选一或同时保留

    List<Menu> menuList;
    if (isSuperAdmin) {
        menuList = menuMapper.selectList(
            new QueryWrapper<Menu>()
                .eq("status", 1)
                .eq("isDelete", 0)
                .in("menuType", 1, 2)   // 侧栏：目录+菜单，按钮可不返回
                .orderByAsc("sort"));
    } else {
        // 2. 普通角色：按 role_menu 聚合去重
        List<Long> roleIds = roles.stream().map(Role::getId).collect(toList());
        menuList = menuMapper.selectMenusByRoleIds(roleIds);
    }

    return buildTree(menuList, 0L);
}
```

### 分配菜单给普通角色

后台接口：`POST /role/assignMenu`

- 入参：`roleId` + `menuId` 列表
- **校验**：若 `role.isSuperAdmin == 1`，拒绝分配（或提示「超管无需配置菜单」）

---

## 四、与 JWT / 登录的衔接

| 项 | 建议 |
|----|------|
| JWT 载荷 | 可放 `roleCodes: ["editor"]` 或主角色；菜单接口以 **查库** 为准，不仅信 Token |
| 登录后前端 | 调 `GET /menu/list/my/tree` 渲染侧栏 |
| `@AuthCheck` | 仍可用 `mustRole`；细粒度可改为校验 `permCode` |

超级管理员判断建议 **以数据库角色为准**（`getLoginUser` 已查库），与菜单查询一致。

---

## 五、前端侧栏数据示例

```json
{
  "code": 0,
  "data": [
    {
      "id": "1",
      "menuName": "系统管理",
      "path": "/system",
      "icon": "setting",
      "children": [
        {
          "id": "2",
          "menuName": "用户管理",
          "path": "/system/user",
          "component": "system/user/index"
        }
      ]
    }
  ]
}
```

---

## 六、角色类型建议划分

| 类型 | 示例 | 菜单策略 |
|------|------|----------|
| 超级管理员 | `admin` | `isSuperAdmin=1`，代码返回全部菜单 |
| 业务角色 | `editor`、`auditor` | 后台勾选 `role_menu` |
| 默认角色 | `user` | 仅前台菜单或空 |
| 惩罚角色 | `ban` | 无菜单，登录拦截 |

角色可以 **持续新增**，不必改枚举；`UserRoleEnum` 可逐步弱化，改为读 `role` 表。

---

## 七、实施步骤建议

| 步骤 | 内容 |
|------|------|
| 1 | 建 `role`、`menu`、`user_role`、`role_menu` |
| 2 | 迁移 `user.userRole` → `user_role` |
| 3 | `admin` 设 `isSuperAdmin=1`，**不写 role_menu** |
| 4 | 实现 `MenuService.getMyMenuTree` + 超管短路逻辑 |
| 5 | 后台：角色 CRUD、普通角色分配菜单 |
| 6 | 前端：登录后拉菜单树渲染侧栏 |

---

## 八、常见坑

1. **给 admin 全量插入 role_menu**：新菜单上线后 admin 看不到，除非再分配 → 应用超管短路。
2. **只返回子菜单不返回父目录**：侧栏断层 → 子菜单可见时父 `parentId` 也要包含。
3. **按钮级 menuType=3 塞进侧栏**：侧栏只返回目录+菜单，按钮用于页面内 `v-if` 权限。
4. **JWT 只存一个 role**：多角色用户菜单应是 **多角色菜单并集**。

---

## 相关文档

- [Q011 角色表字段设计](./Q011-role-table-design.md)
- 当前 `UserRoleEnum`、`AuthInterceptor`、`user.userRole`
