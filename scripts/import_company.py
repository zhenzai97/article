#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""从 CMS 导出的 JSON 导入会员单位到 company 表。"""

import json
import sys
from datetime import datetime, timezone
from pathlib import Path

import pymysql

JSON_PATH = Path(__file__).resolve().parents[1] / "json" / "cms-data-export-company.json"

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


def clear_company(cursor, conn):
    cursor.execute("DELETE FROM company")
    conn.commit()
    print("已清空 company 表")


def parse_company(item):
    source_id = item.get("_id")
    if not source_id:
        raise ValueError(f"缺少 _id, name={item.get('name')}")
    name = (item.get("name") or "").strip()
    if not name:
        raise ValueError(f"公司名称为空, _id={source_id}")

    create_time = ms_to_datetime(item.get("_createTime")) or datetime.now()
    update_time = ms_to_datetime(item.get("_updateTime")) or create_time

    return {
        "name": name,
        "nickname": item.get("nickname") or None,
        "cover": item.get("cover") or None,
        "intro": item.get("intro") or None,
        "vip": to_int(item.get("position")),
        "vipType": to_int(item.get("type")),
        "identity": to_int(item.get("identity")),
        "sex": to_int(item.get("sex"), 0),
        "examineStatus": to_int(item.get("examine_status"), 0),
        "status": to_int_bool(item.get("status"), 0),
        "mobile": item.get("mobile") or None,
        "address": item.get("address") or None,
        "business": item.get("business") or None,
        "sort": to_int(item.get("sort"), 0) or 0,
        "recruitmentDes": item.get("recruitment_des") or None,
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

    print(f"读取企业 {len(items)} 条，来源: {json_path}")

    conn = pymysql.connect(**DB_CONFIG)
    try:
        with conn.cursor() as cursor:
            if clear_first:
                clear_company(cursor, conn)

            insert_sql = """
                INSERT INTO company (
                    name, nickname, cover, intro, vip, vipType, identity, sex,
                    examineStatus, status, mobile, address, business, sort,
                    recruitmentDes, sourceId, createTime, updateTime
                ) VALUES (
                    %(name)s, %(nickname)s, %(cover)s, %(intro)s, %(vip)s, %(vipType)s,
                    %(identity)s, %(sex)s, %(examineStatus)s, %(status)s, %(mobile)s,
                    %(address)s, %(business)s, %(sort)s, %(recruitmentDes)s, %(sourceId)s,
                    %(createTime)s, %(updateTime)s
                )
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    nickname = VALUES(nickname),
                    cover = VALUES(cover),
                    intro = VALUES(intro),
                    vip = VALUES(vip),
                    vipType = VALUES(vipType),
                    identity = VALUES(identity),
                    sex = VALUES(sex),
                    examineStatus = VALUES(examineStatus),
                    status = VALUES(status),
                    mobile = VALUES(mobile),
                    address = VALUES(address),
                    business = VALUES(business),
                    sort = VALUES(sort),
                    recruitmentDes = VALUES(recruitmentDes),
                    updateTime = VALUES(updateTime)
            """

            success = 0
            failed = 0
            batch = []
            batch_size = 50
            failed_msgs = []

            for item in items:
                try:
                    row = parse_company(item)
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
            cursor.execute("SELECT COUNT(*) FROM company")
            total = cursor.fetchone()[0]
            print(f"导入完成: 成功 {success} 条, 失败 {failed} 条, 数据库共 {total} 条")
    finally:
        conn.close()


if __name__ == "__main__":
    main()
