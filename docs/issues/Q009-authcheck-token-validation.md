# Q009：@AuthCheck 接口（如 addUser）如何校验 Token？

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

`POST /user/add` 带有 `@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)`，该方法本身没有写 Token 校验代码，JWT 是如何生效的？

---

## 结论

**Controller 方法里不需要再手写 Token 校验**。  
请求进入 `addUser` **之前**，`AuthInterceptor` 切面会先执行，内部调用 `userService.getLoginUser(request)`，从请求头解析 JWT 并校验管理员角色。

---

## 完整调用链

```
客户端 POST /api/user/add
  Header: Authorization: Bearer <token>
        ↓
AuthInterceptor.doInterceptor()     ← @AuthCheck 触发 AOP
        ↓
userService.getLoginUser(request)
        ↓
jwtUtils.getUserIdFromRequest(request)   ← 从 Header 取 Token
jwtUtils.decodeToken(token)              ← 校验签名、过期
this.getById(userId)                     ← 查库拿最新角色
        ↓
校验 loginUser.userRole == admin ?
  否 → 40101 NO_AUTH_ERROR
  是 → joinPoint.proceed() 进入 addUser()
```

---

## 关键代码

### 1. Controller（只声明权限，不校验 Token）

```java
@PostMapping("/add")
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
    // 能执行到这里，说明 Token 有效且已是 admin
    ...
}
```

`HttpServletRequest request` 参数在本方法内 **未使用** 也正常；鉴权在 AOP 中已完成。

### 2. AuthInterceptor（真正校验 Token + 角色）

```java
@Around("@annotation(authCheck)")
public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) {
    HttpServletRequest request = ...;
    User loginUser = userService.getLoginUser(request);  // JWT 解析
    // 校验 mustRole 是否为 admin ...
    return joinPoint.proceed();
}
```

### 3. getLoginUser（JWT 取用户）

```java
Long userId = jwtUtils.getUserIdFromRequest(request);
User currentUser = this.getById(userId);
// 封号用户拒绝 ...
return currentUser;
```

---

## 前端 / 测试怎么调

1. 用 **管理员账号** 登录，拿到 `token`
2. 调用添加用户接口时带请求头：

```
POST /api/user/add
Authorization: Bearer <admin 的 token>
Content-Type: application/json

{ "userAccount": "testuser", ... }
```

### 常见结果

| 情况 | 错误码 |
|------|--------|
| 未带 Token / Token 无效 / 过期 | `40100` 未登录 |
| Token 有效但角色是 `user` | `40101` 无权限 |
| Token 有效且角色是 `admin` | 正常创建用户 |

---

## 若要在 Controller 内再次获取当前用户

一般 **不必重复校验**。若业务需要当前操作人 id，可注入调用：

```java
User loginUser = userService.getLoginUser(request);
```

AOP 已校验过，此处会直接解析同一 Token（多一次解析，通常可接受）。

---

## 与「无 @AuthCheck 但需登录」接口的区别

| 类型 | 示例 | Token 校验方式 |
|------|------|----------------|
| 管理员接口 | `@AuthCheck(mustRole = ADMIN)` | AOP 自动校验 Token + 角色 |
| 登录即可 | 发帖、点赞 | Controller 内 `getLoginUser(request)` |
| 公开接口 | 帖子列表 | 不校验；可选 `getLoginUserPermitNull` |

---

## 相关文件

- `UserController.java` — `addUser` + `@AuthCheck`
- `AuthInterceptor.java` — 权限切面
- `UserServiceImpl.java` — `getLoginUser`
- `JwtUtils.java` — `getTokenFromRequest`、`decodeToken`
