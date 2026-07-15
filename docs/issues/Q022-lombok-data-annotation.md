# Q022：@Data 注解的作用

- **日期**：2026-07-15
- **状态**：已解答

---

## 问题描述

实体类上的 `@Data` 注解是干什么用的？

---

## 原因分析

`@Data` 来自 **Lombok**，不是 Spring / MyBatis-Plus 注解。  
编译期由 Lombok 插件根据字段自动生成样板代码，避免手写 getter/setter 等。

---

## 解决方案 / 结论

在类上加 `@Data`，约等于同时生成：

| 生成内容 | 等价注解 / 方法 |
|----------|-----------------|
| 所有字段的 getter | `@Getter` |
| 所有字段的 setter | `@Setter` |
| `toString()` | `@ToString` |
| `equals()` / `hashCode()` | `@EqualsAndHashCode` |
| 无参构造以外的便利 | 含 `@RequiredArgsConstructor`（对 `final` / `@NonNull` 字段） |

示例：

```java
@Data
public class AdvertisingSpace {
    private Long id;
    private String title;
}
```

编译后可直接使用 `getId()`、`setTitle(...)` 等，源码里不用写这些方法。

### 注意

1. 需要 IDE 安装 Lombok 插件，且 `pom.xml` 中有 Lombok 依赖。  
2. 若只要 getter/setter，可用 `@Getter` / `@Setter`，而不必用完整 `@Data`。  
3. 与 `@EqualsAndHashCode` 相关的实体继承场景，有时要用 `callSuper = true`（如 QueryRequest 继承 PageRequest）。

---

## 相关文件

- `src/main/java/com/springbootinit/model/entity/AdvertisingSpace.java`
- `src/main/java/com/springbootinit/model/entity/ArticleCat.java`
- `pom.xml`（lombok 依赖）
