# Q020：listMenuTreeVO 接口详细说明

- **日期**：2026-06-27
- **状态**：已解答

---

## 接口信息

| 项 | 值 |
|----|-----|
| 方法 | `GET` |
| 路径 | `/api/menu/list/tree/vo` |
| 权限 | 管理员（`@AuthCheck` + JWT） |
| 返回 | `List<MenuVO>` 树形结构 |

---

## Controller 代码逐行说明

```java
@GetMapping("/list/tree/vo")
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
public BaseResponse<List<MenuVO>> listMenuTreeVO() {
    List<Menu> menuList = menuService.list(menuService.getQueryWrapper(new MenuQueryRequest()));
    return ResultUtils.success(menuService.buildMenuTree(menuList));
}
```

| 步骤 | 代码 | 作用 |
|------|------|------|
| 1 | `@GetMapping` | GET 请求，无 Body |
| 2 | `@AuthCheck(ADMIN)` | 仅 admin 可访问，JWT 校验 |
| 3 | `new MenuQueryRequest()` | 空条件 = 查全部菜单（MyBatis Plus 自动过滤 `isDelete=0`） |
| 4 | `getQueryWrapper(...)` | 组装 SQL 条件（此处无额外 where） |
| 5 | `menuService.list(...)` | 查库得到 **扁平** `List<Menu>` |
| 6 | `buildMenuTree(menuList)` | 按 `parentId` 组装 **树形** `List<MenuVO>` |
| 7 | `ResultUtils.success(...)` | 包装为 `{ code:0, data:[...], message:"ok" }` |

---

## buildMenuTree 算法

### 输入（扁平）

| id | parentId | menuName |
|----|----------|----------|
| 1 | 0 | 系统管理 |
| 2 | 1 | 用户管理 |
| 3 | 1 | 角色管理 |
| 4 | 0 | 内容管理 |

### 步骤

1. **Entity → VO**：`getMenuVO`，此时 `children` 均为 null  
2. **按 parentId 分组**：`parentMap`  
   - `0 → [系统管理, 内容管理]`  
   - `1 → [用户管理, 角色管理]`  
3. **挂 children**：每个节点的 `children = parentMap.get(节点.id)`  
4. **返回根节点**：`parentMap.get(0L)`，即 `parentId=0` 的节点列表  

### 输出（树形 JSON 示意）

```json
[
  {
    "id": "1",
    "menuName": "系统管理",
    "parentId": "0",
    "children": [
      { "id": "2", "menuName": "用户管理", "parentId": "1", "children": null },
      { "id": "3", "menuName": "角色管理", "parentId": "1", "children": null }
    ]
  },
  {
    "id": "4",
    "menuName": "内容管理",
    "parentId": "0",
    "children": null
  }
]
```

---

## 与 `/list/page/vo` 的区别

| | `/list/tree/vo` | `/list/page/vo` |
|--|-----------------|-------------------|
| 结构 | 树形，有 `children` | 扁平分页 |
| 分页 | 无 | 有 current/size |
| 场景 | 菜单管理树表、上级菜单下拉 | 搜索 + 分页 |

---

## 调用示例

```
GET http://localhost:8101/api/menu/list/tree/vo
Authorization: Bearer <admin token>
```

---

## 相关文件

- `MenuController.listMenuTreeVO`
- `MenuServiceImpl.buildMenuTree`
- `MenuVO.children`
