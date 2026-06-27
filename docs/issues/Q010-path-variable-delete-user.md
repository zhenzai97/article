# Q010：DELETE 接口如何从路径取动态参数（/user/delete/12）

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

删除用户接口希望使用路径传参，请求地址为：

```
DELETE /api/user/delete/12
```

其中 `12` 为要删除的用户 id，如何在 Controller 中接收？

---

## 结论

使用 **`@PathVariable`** 绑定 URL 路径中的动态段，配合 **`@DeleteMapping("/delete/{id}")`**。

---

## 代码示例（已实施）

```java
@DeleteMapping("/delete/{id}")
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
public BaseResponse<Boolean> deleteUser(@PathVariable("id") long id, HttpServletRequest request) {
    if (id <= 0) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
    boolean b = userService.removeById(id);
    return ResultUtils.success(b);
}
```

### 说明

| 项 | 说明 |
|----|------|
| `@DeleteMapping("/delete/{id}")` | `{id}` 表示路径变量名 |
| `@PathVariable("id")` | 将路径中的值注入参数 `id` |
| 完整 URL | `context-path` `/api` + `@RequestMapping("/user")` + `/delete/12` → `/api/user/delete/12` |

参数名与 `{id}` 一致时，可简写为 `@PathVariable long id`（省略 `"id"`）。

---

## 与 @RequestBody 的区别

| 方式 | 示例 | 适用 |
|------|------|------|
| **@PathVariable** | `DELETE /user/delete/12` | REST 风格，单个 id |
| **@RequestBody** | `POST /user/delete` + JSON `{"id":12}` | 原鱼皮模板方式 |
| **@RequestParam** | `DELETE /user/delete?id=12` | 查询参数，非路径 |

---

## 调用示例

### Knife4j / Postman

```
DELETE http://localhost:8101/api/user/delete/12
Authorization: Bearer <admin token>
```

无需 Body。

### Axios

```javascript
axios.delete(`/api/user/delete/${userId}`, {
  headers: { Authorization: `Bearer ${token}` }
});
```

### curl

```bash
curl -X DELETE "http://localhost:8101/api/user/delete/12" \
  -H "Authorization: Bearer eyJ..."
```

---

## Token 校验

与 `addUser` 相同：`@AuthCheck` → `AuthInterceptor` → `getLoginUser(request)` 解析 JWT，**无需在方法内再写 Token 逻辑**。

---

## 相关文件

- `UserController.java` — `deleteUser` 方法
