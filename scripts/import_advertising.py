#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""从 CMS 导出的 JSON 导入运营广告到 advertising 表。"""

import json
import sys
from datetime import datetime, timezone
from pathlib import Path

import pymysql

JSON_PATH = Path(__file__).resolve().parents[1] / "json" / "cms-data-export-advertising.json"

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


def parse_date(value):
    if not value:
        return None
    if isinstance(value, datetime):
        return value.date()
    text = str(value).strip()
    if not text:
        return None
    return datetime.strptime(text[:10], "%Y-%m-%d").date()


def to_int_bool(value, default=0):
    if value is None:
        return default
    return 1 if value else 0


def load_space_map(cursor):
    cursor.execute("SELECT sign, id FROM advertising_space")
    return dict(cursor.fetchall())


def clear_advertising(cursor, conn):
    cursor.execute("DELETE FROM advertising")
    conn.commit()
    print("已清空 advertising 表")


def parse_advertising(item, space_map):
    cate = item.get("cate_id") or {}
    sign = cate.get("sign")
    space_id = space_map.get(sign)
    if space_id is None:
        raise ValueError(f"运营位 sign 不存在: {sign}, name={item.get('name')}")

    return {
        "spaceId": space_id,
        "name": item.get("name") or "",
        "cover": item.get("cover"),
        "video": item.get("video"),
        "path": item.get("path"),
        "content": item.get("content"),
        "remark": item.get("desc"),
        "sort": item.get("sort") or 0,
        "status": to_int_bool(item.get("status"), 1),
        "startTime": parse_date(item.get("start_time")),
        "endTime": parse_date(item.get("end_time")),
        "createTime": ms_to_datetime(item.get("_createTime")),
        "updateTime": ms_to_datetime(item.get("_updateTime")),
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

    print(f"读取广告 {len(items)} 条，来源: {json_path}")

    conn = pymysql.connect(**DB_CONFIG)
    try:
        with conn.cursor() as cursor:
            if clear_first:
                clear_advertising(cursor, conn)

            space_map = load_space_map(cursor)
            print(f"运营位映射 {len(space_map)} 个")

            insert_sql = """
                INSERT INTO advertising (
                    spaceId, name, cover, video, path, content, remark, sort,
                    status, startTime, endTime, createTime, updateTime
                ) VALUES (
                    %(spaceId)s, %(name)s, %(cover)s, %(video)s, %(path)s, %(content)s,
                    %(remark)s, %(sort)s, %(status)s, %(startTime)s, %(endTime)s,
                    %(createTime)s, %(updateTime)s
                )
            """

            success = 0
            failed = 0
            batch = []
            batch_size = 100
            failed_msgs = []

            for item in items:
                try:
                    row = parse_advertising(item, space_map)
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
            cursor.execute("SELECT COUNT(*) FROM advertising")
            total = cursor.fetchone()[0]
            print(f"导入完成: 成功 {success} 条, 失败 {failed} 条, 数据库共 {total} 条")
    finally:
        conn.close()


if __name__ == "__main__":
    main()
