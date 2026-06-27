# Q017：MenuController 中 MenuService.getMenuVOPage 静态调用错误

- **日期**：2026-06-27
- **状态**：已解答

---

## 问题描述

```java
return ResultUtils.success(MenuService.getMenuVOPage(menuPage));
```

提示：**无法从 static 上下文引用非 static 方法 `getMenuVOPage`**。

---

## 原因

- `MenuService` 是 **接口/类名**，这样写表示 **静态方法调用**。
- `getMenuVOPage` 是 **实例方法**，需要通过 Spring 注入的 Bean 调用。

Controller 中已注入：

```java
@Resource
private MenuService menuService;
```

应使用 **小写** 的 `menuService`（实例），不是 **大写** 的 `MenuService`（类名）。

---

## 正确写法

```java
return ResultUtils.success(menuService.getMenuVOPage(menuPage));
```

---

## 顺带修复

原代码两个方法都用了 `@PostMapping("/list/page")`，路径冲突。VO 分页接口应改为：

```java
@PostMapping("/list/page/vo")
```

---

## 相关文件

- `MenuController.java`
- `MenuService.java`
