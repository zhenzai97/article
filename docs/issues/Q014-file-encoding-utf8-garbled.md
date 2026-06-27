# Q014：RoleController 提示「该文件以错误的编码加载 UTF-8」

- **日期**：2026-06-27
- **状态**：已解答

---

## 问题描述

`RoleController.java` 等由代码生成器生成的文件，IDE 提示：**该文件以错误的编码加载: 'UTF-8'**，中文注释显示为乱码（如 `ɫӿ`）。

---

## 原因

`CodeGenerator.doGenerate()` 使用了 **`new FileWriter(outputPath)`**。

在 **Windows** 上，`FileWriter` 默认使用 **系统编码（GBK）** 写入文件，而：

- 项目 / IDE 默认以 **UTF-8** 打开 Java 源文件
- FreeMarker 模板是 UTF-8，中文注释在写入时被按 GBK 编码存盘

结果：文件实际是 GBK 字节，IDE 用 UTF-8 解码 → 乱码 + 编码警告。

这与 Maven 编译时的「无法映射 UTF-8 字符」警告是同一根因。

---

## 解决方案（已修复）

将 `FileWriter` 改为 **显式 UTF-8** 写入：

```java
Writer out = new OutputStreamWriter(
        new FileOutputStream(outputPath), StandardCharsets.UTF_8);
```

并已重新运行 `CodeGenerator.generateRole()`，覆盖 `src/main/java` 下所有 Role 相关文件。

---

## 如何避免再次出现

1. 代码生成器 **禁止** 使用无编码参数的 `FileWriter`
2. IDE 确认项目编码为 UTF-8（Settings → Editor → File Encodings）
3. `pom.xml` 中保持：

```xml
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
```

---

## 若 IDE 仍提示

1. 关闭 `RoleController.java` 标签页
2. 重新打开（或 Reload from Disk）
3. 不要用「以 GBK 重新加载」——文件已改为 UTF-8

---

## 相关文件

- `CodeGenerator.java` — `doGenerate` 写入编码
- `src/main/java/com/springbootinit/controller/RoleController.java` 等 Role 模块文件
