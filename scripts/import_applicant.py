#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""从 CMS 导出的 JSON 导入应聘到 applicant 表。"""

import json
import sys
from datetime import datetime, timezone
from pathlib import Path

import pymysql

JSON_PATH = Path(__file__).resolve().parents[1] / "json" / "cms-data-export-applicant.json"

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


def load_company_map(cursor):
    cursor.execute(
        "SELECT sourceId, id, name FROM company WHERE sourceId IS NOT NULL AND isDelete = 0"
    )
    return {sid: (cid, name) for sid, cid, name in cursor.fetchall()}


def load_recruitment_map(cursor):
    cursor.execute(
        "SELECT sourceId, id, name FROM recruitment WHERE sourceId IS NOT NULL AND isDelete = 0"
    )
    return {sid: (rid, name) for sid, rid, name in cursor.fetchall()}


def clear_applicant(cursor, conn):
    cursor.execute("DELETE FROM applicant")
    conn.commit()
    print("已清空 applicant 表")


def parse_applicant(item, company_map, recruitment_map):
    source_id = item.get("_id")
    if not source_id:
        raise ValueError(f"缺少 _id, name={item.get('name')}")
    name = (item.get("name") or "").strip()
    if not name:
        raise ValueError(f"应聘人姓名为空, _id={source_id}")

    company = item.get("company_id") or {}
    company_id = None
    company_name = None
    if isinstance(company, dict):
        company_name = company.get("name")
        csid = company.get("_id")
        if csid:
            mapped = company_map.get(csid)
            if mapped:
                company_id, mapped_name = mapped
                if not company_name:
                    company_name = mapped_name
            else:
                raise ValueError(f"公司 sourceId 不存在: {csid}, applicant={name}")

    # 旧 CMS 字段拼写为 jop_id（job）
    job = item.get("jop_id") or item.get("job_id") or {}
    recruitment_id = None
    recruitment_name = None
    if isinstance(job, dict):
        recruitment_name = job.get("name")
        jsid = job.get("_id")
        if jsid:
            mapped = recruitment_map.get(jsid)
            if mapped:
                recruitment_id, mapped_name = mapped
                if not recruitment_name:
                    recruitment_name = mapped_name
            # 岗位找不到时不强失败：部分旧数据 jop_id 缺失或未导入
            # else: leave null

    create_time = ms_to_datetime(item.get("_createTime")) or datetime.now()
    update_time = ms_to_datetime(item.get("_updateTime")) or create_time

    return {
        "name": name,
        "mobile": item.get("candidate_phone") or None,
        "salaryRange": item.get("salary_range") or None,
        "companyId": company_id,
        "companyName": company_name,
        "recruitmentId": recruitment_id,
        "recruitmentName": recruitment_name,
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

    print(f"读取应聘 {len(items)} 条，来源: {json_path}")

    conn = pymysql.connect(**DB_CONFIG)
    try:
        with conn.cursor() as cursor:
            if clear_first:
                clear_applicant(cursor, conn)

            company_map = load_company_map(cursor)
            recruitment_map = load_recruitment_map(cursor)
            print(f"公司映射 {len(company_map)} 个, 岗位映射 {len(recruitment_map)} 个")

            insert_sql = """
                INSERT INTO applicant (
                    name, mobile, salaryRange, companyId, companyName,
                    recruitmentId, recruitmentName, sourceId, createTime, updateTime
                ) VALUES (
                    %(name)s, %(mobile)s, %(salaryRange)s, %(companyId)s, %(companyName)s,
                    %(recruitmentId)s, %(recruitmentName)s, %(sourceId)s, %(createTime)s, %(updateTime)s
                )
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    mobile = VALUES(mobile),
                    salaryRange = VALUES(salaryRange),
                    companyId = VALUES(companyId),
                    companyName = VALUES(companyName),
                    recruitmentId = VALUES(recruitmentId),
                    recruitmentName = VALUES(recruitmentName),
                    updateTime = VALUES(updateTime)
            """

            success = 0
            failed = 0
            batch = []
            batch_size = 50
            failed_msgs = []
            no_job = 0

            for item in items:
                try:
                    row = parse_applicant(item, company_map, recruitment_map)
                    if row["recruitmentId"] is None:
                        no_job += 1
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
            cursor.execute("SELECT COUNT(*) FROM applicant")
            total = cursor.fetchone()[0]
            print(
                f"导入完成: 成功 {success} 条, 失败 {failed} 条, "
                f"无岗位关联 {no_job} 条, 数据库共 {total} 条"
            )
    finally:
        conn.close()


if __name__ == "__main__":
    main()
