# Q018：MenuServiceImpl.getMenuVO 的 @Override 提示未重写方法

- **日期**：2026-06-27
- **状态**：已解答

---

## 问题描述

`MenuServiceImpl` 中：

```java
@Override
public MenuVO getMenuVO(Menu menu) { ... }
```

IDE 提示：**方法未从其超类重写方法**（Pull method 'getMenuVO' to 'MenuService'）。

---

## 原因

`@Override` 表示该方法 **重写父类或接口中的方法**。

当前情况：

- `MenuServiceImpl` **实现了** `getMenuVO`
- `MenuService` 接口里 **只有** `getMenuVOPage`，**没有** 声明 `getMenuVO`

接口中没有对应方法，编译器认为这不是重写，因此 `@Override` 报错。

---

## 解决方案

在 `MenuService` 接口中补充方法声明：

```java
/**
 * 获取菜单封装
 */
MenuVO getMenuVO(Menu menu);
```

实现类保留 `@Override` 即可，与 `RoleService` / `RoleServiceImpl` 写法一致。

---

## 对比 Role 模块（正确示例）

`RoleService.java`：

```java
RoleVO getRoleVO(Role role);
Page<RoleVO> getRoleVOPage(Page<Role> rolePage);
```

`RoleServiceImpl` 中两个方法都有 `@Override`。

Menu 模块之前漏了 `getMenuVO` 的接口声明。

---

## 相关文件

- `MenuService.java`（已补充 `getMenuVO`）
- `MenuServiceImpl.java`
