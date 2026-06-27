# Q003：为 JwtUtils.java 方法生成注释

- **日期**：2026-06-26
- **状态**：已解答

---

## 问题描述

为 `JwtUtils.java` 第 17～43 行（`generateToken`、`decodeToken`、`getTokenFromRequest`）生成注释。

---

## 处理结果

已按项目内 `NetUtils` 等工具类的 JavaDoc 风格补充：

| 位置 | 注释内容 |
|------|----------|
| 类 | JWT 工具类说明 |
| `CLAIM_USER_ID` / `CLAIM_USER_ROLE` | Token 载荷字段名说明 |
| `generateToken` | 参数（userId、userRole、secret、expireMs）与返回值 |
| `decodeToken` | 校验签名与过期、返回值、异常说明 |
| `getTokenFromRequest` | 从 Authorization 头提取 Bearer Token 的规则 |

---

## 相关文件

- `src/main/java/com/springbootinit/utils/JwtUtils.java`
