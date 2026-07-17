#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""从 CMS 导出的 JSON 导入招聘到 recruitment 表。"""

import json
import sys
from datetime import datetime, timezone
from pathlib import Path

import pymysql

JSON_PATH = Path(__file__).resolve().parents[1] / "json" / "cms-data-export-recruitment.json"

DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "admin123",
    "database": "my_article",
    "charset": "utf8mb4",
}


def ms_to_datetime(ms):
    if ms is None:
        return None
    return datetime.fromtimestamp(ms / 1000, tz=timezone.utc).replace(tzinfo=None)


def to_int(value, default=None):
    if value is None or value == "":
        return default
    try:
        return int(value)
    except (TypeError, ValueError):
        return default


def to_int_bool(value, default=0):
    if value is None:
        return default
    if isinstance(value, bool):
        return 1 if value else 0
    if isinstance(value, (int, float)):
        return 1 if value else 0
    text = str(value).strip().lower()
    if text in ("1", "true", "yes"):
        return 1
    if text in ("0", "false", "no"):
        return 0
    return default


def build_salary_range(st, ed):
    st_i = to_int(st)
    ed_i = to_int(ed)
    if st_i is not None and ed_i is not None:
        return f"{st_i}-{ed_i}"
    if st_i is not None:
        return str(st_i)
    if ed_i is not None:
        return str(ed_i)
    return None


def load_company_map(cursor):
    cursor.execute("SELECT sourceId, id, name FROM company WHERE sourceId IS NOT NULL AND isDelete = 0")
    by_source = {}
    for source_id, company_id, name in cursor.fetchall():
        by_source[source_id] = (company_id, name)
    return by_source


def clear_recruitment(cursor, conn):
    cursor.execute("DELETE FROM recruitment")
    conn.commit()
    print("已清空 recruitment 表")


def parse_recruitment(item, company_map):
    source_id = item.get("_id")
    if not source_id:
        raise ValueError(f"缺少 _id, name={item.get('name')}")
    name = (item.get("name") or "").strip()
    if not name:
        raise ValueError(f"岗位名称为空, _id={source_id}")

    company = item.get("company_name") or {}
    if not isinstance(company, dict):
        raise ValueError(f"company_name 格式错误, _id={source_id}")
    company_source_id = company.get("_id")
    company_name = company.get("name")
    company_id = None
    if company_source_id:
        mapped = company_map.get(company_source_id)
        if mapped:
            company_id, mapped_name = mapped
            if not company_name:
                company_name = mapped_name
        else:
            raise ValueError(
                f"公司 sourceId 不存在: {company_source_id}, job={name}"
            )

    create_time = ms_to_datetime(item.get("_createTime")) or datetime.now()
    update_time = ms_to_datetime(item.get("_updateTime")) or create_time

    return {
        "name": name,
        "salaryRange": build_salary_range(
            item.get("salary_range_st"), item.get("salary_range_ed")
        ),
        "workingHours": item.get("workinghours") or None,
        "qualification": item.get("qualification") or None,
        "companyId": company_id,
        "companyName": company_name or None,
        "sort": to_int(item.get("sort"), 0) or 0,
        "status": to_int_bool(item.get("status"), 0),
        "salaryContent": item.get("salary_details") or None,
        "positionContent": item.get("job_respons") or None,
        "employeeWelfare": item.get("employee_welfar") or None,
        "sourceId": source_id,
        "createTime": create_time,
        "updateTime": update_time,
    }


def main():
    args = sys.argv[1:]
    clear_first = False
    json_path = JSON_PATH

    for arg in args:
        if arg in ("--clear", "-c"):
            clear_first = True
        else:
            json_path = Path(arg)

    if not json_path.exists():
        print(f"文件不存在: {json_path}")
        sys.exit(1)

    with json_path.open("r", encoding="utf-8") as f:
        items = json.load(f)

    print(f"读取招聘 {len(items)} 条，来源: {json_path}")

    conn = pymysql.connect(**DB_CONFIG)
    try:
        with conn.cursor() as cursor:
            if clear_first:
                clear_recruitment(cursor, conn)

            company_map = load_company_map(cursor)
            print(f"公司映射 {len(company_map)} 个")

            insert_sql = """
                INSERT INTO recruitment (
                    name, salaryRange, workingHours, qualification,
                    companyId, companyName, sort, status,
                    salaryContent, positionContent, employeeWelfare,
                    sourceId, createTime, updateTime
                ) VALUES (
                    %(name)s, %(salaryRange)s, %(workingHours)s, %(qualification)s,
                    %(companyId)s, %(companyName)s, %(sort)s, %(status)s,
                    %(salaryContent)s, %(positionContent)s, %(employeeWelfare)s,
                    %(sourceId)s, %(createTime)s, %(updateTime)s
                )
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    salaryRange = VALUES(salaryRange),
                    workingHours = VALUES(workingHours),
                    qualification = VALUES(qualification),
                    companyId = VALUES(companyId),
                    companyName = VALUES(companyName),
                    sort = VALUES(sort),
                    status = VALUES(status),
                    salaryContent = VALUES(salaryContent),
                    positionContent = VALUES(positionContent),
                    employeeWelfare = VALUES(employeeWelfare),
                    updateTime = VALUES(updateTime)
            """

            success = 0
            failed = 0
            batch = []
            batch_size = 50
            failed_msgs = []

            for item in items:
                try:
                    row = parse_recruitment(item, company_map)
                    batch.append(row)
                    if len(batch) >= batch_size:
                        cursor.executemany(insert_sql, batch)
                        conn.commit()
                        success += len(batch)
                        batch.clear()
                        print(f"已导入 {success} 条...")
                except Exception as e:
                    failed += 1
                    failed_msgs.append(str(e))

            if batch:
                cursor.executemany(insert_sql, batch)
                conn.commit()
                success += len(batch)

            if failed_msgs:
                print(f"失败 {failed} 条，示例: {failed_msgs[0]}")
                for msg in failed_msgs[1:5]:
                    print(f"  - {msg}")
            cursor.execute("SELECT COUNT(*) FROM recruitment")
            total = cursor.fetchone()[0]
            print(f"导入完成: 成功 {success} 条, 失败 {failed} 条, 数据库共 {total} 条")
    finally:
        conn.close()


if __name__ == "__main__":
    main()
