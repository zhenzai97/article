# Q019：菜单列表应返回树形还是分页扁平数据？

- **日期**：2026-06-27
- **状态**：已解答

---

## 问题描述

`getMenuVOPage` 当前返回扁平 `Page<MenuVO>`，菜单是否应该返回树形数据？

---

## 结论

**两种都要，用途不同**：

| 方法/接口 | 返回 | 适用场景 |
|-----------|------|----------|
| `getMenuVOPage` + `POST /menu/list/page/vo` | 扁平分页 | 条件搜索、大数据量分页 |
| `buildMenuTree` + `GET /menu/list/tree/vo` | **树形** | 后台菜单管理页、上级菜单下拉、侧栏 |

**不建议**在 `getMenuVOPage` 里做树形：分页会截断父子节点（第 1 页可能只有子菜单没有父级），树结构会乱。

---

## 树形构建逻辑（已实施）

```java
public List<MenuVO> buildMenuTree(List<Menu> menuList) {
    List<MenuVO> voList = menuList.stream().map(this::getMenuVO).collect(...);
    Map<Long, List<MenuVO>> parentMap = voList.stream()
        .collect(Collectors.groupingBy(vo -> vo.getParentId() == null ? 0L : vo.getParentId()));
    voList.forEach(vo -> vo.setChildren(parentMap.get(vo.getId())));
    return parentMap.getOrDefault(0L, new ArrayList<>());  // parentId=0 为根
}
```

返回示例：

```json
[
  {
    "id": "1",
    "menuName": "系统管理",
    "parentId": "0",
    "children": [
      { "id": "2", "menuName": "用户管理", "parentId": "1", "children": null }
    ]
  }
]
```

---

## 前端调用建议

- 菜单管理表格（树形表格）→ `GET /api/menu/list/tree/vo`
- 带搜索的分页列表 → `POST /api/menu/list/page/vo`

---

## 相关文件

- `MenuServiceImpl.buildMenuTree`
- `MenuController.listMenuTreeVO`
- `MenuVO.children`
