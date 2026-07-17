# Q027：招聘 salaryRange / workingHours / qualification 字段约定

- **日期**：2026-07-17
- **状态**：已解答

---

## 问题描述

确认招聘三个字段保存时直接传字符串，与前端约定枚举值；工作经验、学历由前端维护枚举。

---

## 结论

三个字段均为 **`varchar` 字符串**，后端不做拆分、不存数字起止，原样入库。

| 字段 | 类型 | 示例 | 说明 |
|------|------|------|------|
| `salaryRange` | String | `5000-6000` | 薪资范围，前后端约定格式 |
| `workingHours` | String | `1-3年` | 工作经验，前端枚举 |
| `qualification` | String | `大学本科` | 学历，前端枚举 |

### workingHours 前端枚举

- 1-3年
- 3-5年
- 5-10年
- 10-15年
- 15-20年
- 无经验

### qualification 前端枚举

- 小学
- 初中
- 高中
- 大专
- 大学本科
- 研究生
- 博士研究生

### 旧 CMS 导入

旧数据 `salary_range_st` / `salary_range_ed` 导入时合并为 `salaryRange` 字符串（如 `6000-10000`），不单独建数字列。

---

## 相关文件

- `sql/create_table.sql`（`recruitment` 表）
- `model/entity/Recruitment.java`
- `model/dto/recruitment/RecruitmentAddRequest.java`
