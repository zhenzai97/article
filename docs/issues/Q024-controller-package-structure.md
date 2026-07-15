# Q024：Controller 是全部放一个包，还是按功能分包？

- **日期**：2026-07-15
- **状态**：已解答

---

## 问题描述

`src/main/java/com/springbootinit/controller` 下所有 Controller 都放在一起，不用分类？标准写法是按功能分开，还是全部写在一起？

---

## 结论

**两种都对，取决于项目规模。本项目当前用「按层分包、Controller 平铺」即可。**

---

## 两种常见写法

### 1. 按技术层分包（当前项目）

```
com.springbootinit
├── controller/          # 所有 Controller 平铺
│   ├── UserController
│   ├── ArticleController
│   ├── ArticleCatController
│   └── AdvertisingSpaceController
├── service/
├── mapper/
└── model/
```

- **适合**：中小型后台、模板型项目（如 springboot-init）
- **优点**：结构简单，和框架默认习惯一致，查找同层代码方便
- **缺点**：模块变多时，一个包里类会很多

### 2. 按业务功能分包（模块化）

```
com.springbootinit
├── module
│   ├── article
│   │   ├── ArticleController
│   │   ├── ArticleService
│   │   └── ArticleMapper
│   ├── user
│   │   ├── UserController
│   │   └── ...
│   └── advertising
│       └── ...
```

或只在 controller 下按功能分子包：

```
controller/
├── article/ArticleController.java
├── user/UserController.java
└── system/RoleController.java
```

- **适合**：中大型、多业务域、多人协作
- **优点**：业务边界清晰，利于拆分微服务
- **缺点**：前期包路径更长，对小项目略重

---

## 对本项目的建议

| 阶段 | 建议 |
|------|------|
| 现在（文章 / 分类 / 广告位 / 角色菜单） | **全部平铺在 `controller/`**，与现有 `service`、`mapper`、`model` 一致 |
| 模块明显增多（例如 20+ Controller）或团队按业务拆组 | 再考虑按业务分包，或迁到 `module.xxx` |

**没有「标准必须按功能分文件夹」的强制规范。**  
Spring 只要求类上有 `@RestController` 且被扫到；包名是否再分，是团队约定，不是框架硬性要求。

本仓库（鱼皮 init 风格）惯例就是：

- `controller` / `service` / `mapper` / `model` **按层放**
- 同一层内按类名区分功能（`Article*`、`User*`），**不必再套一层目录**

---

## 相关文件

- `src/main/java/com/springbootinit/controller/`
- 对比：`service/`、`mapper/`、`model/` 同样是按层平铺
