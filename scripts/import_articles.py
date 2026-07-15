#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""从 CMS 导出的 JSON 导入文章到 article 表。"""

import json
import sys
from datetime import datetime, timezone
from pathlib import Path

import pymysql

JSON_PATH = Path(__file__).resolve().parents[1] / "json" / "cms-data-export-2026-07-11-30654.json"

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


def to_int_bool(value, default=0):
    if value is None:
        return default
    return 1 if value else 0


def load_category_map(cursor):
    cursor.execute("SELECT sign, id FROM article_category")
    return dict(cursor.fetchall())


def parse_article(item, category_map):
    cate = item.get("cate_id") or {}
    sign = cate.get("sign")
    category_id = category_map.get(sign)
    if category_id is None:
        raise ValueError(f"分类 sign 不存在: {sign}, title={item.get('title')}")

    return {
        "categoryId": category_id,
        "title": item.get("title") or "",
        "content": item.get("content") or "",
        "cover": item.get("cover"),
        "video": item.get("video"),
        "sort": item.get("sort") or 0,
        "readNum": item.get("read") or 0,
        "status": to_int_bool(item.get("status"), 1),
        "isHome": to_int_bool(item.get("is_home"), 0),
        "isTop": to_int_bool(item.get("is_top"), 0),
        "createTime": ms_to_datetime(item.get("_createTime")),
        "updateTime": ms_to_datetime(item.get("_updateTime")),
    }


def clear_articles(cursor, conn):
    cursor.execute("DELETE FROM article")
    conn.commit()
    print("已清空 article 表")


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
        articles = json.load(f)

    print(f"读取文章 {len(articles)} 条，来源: {json_path}")

    conn = pymysql.connect(**DB_CONFIG)
    try:
        with conn.cursor() as cursor:
            if clear_first:
                clear_articles(cursor, conn)

            category_map = load_category_map(cursor)
            print(f"分类映射 {len(category_map)} 个")

            insert_sql = """
                INSERT INTO article (
                    categoryId, title, content, cover, video, sort, readNum,
                    status, isHome, isTop, createTime, updateTime
                ) VALUES (
                    %(categoryId)s, %(title)s, %(content)s, %(cover)s, %(video)s,
                    %(sort)s, %(readNum)s, %(status)s, %(isHome)s, %(isTop)s,
                    %(createTime)s, %(updateTime)s
                )
            """

            success = 0
            failed = 0
            batch = []
            batch_size = 100
            failed_titles = []

            for item in articles:
                try:
                    row = parse_article(item, category_map)
                    batch.append(row)
                    if len(batch) >= batch_size:
                        cursor.executemany(insert_sql, batch)
                        conn.commit()
                        success += len(batch)
                        batch.clear()
                        print(f"已导入 {success} 条...")
                except Exception as e:
                    failed += 1
                    failed_titles.append(str(e))

            if batch:
                cursor.executemany(insert_sql, batch)
                conn.commit()
                success += len(batch)

            if failed_titles:
                print(f"失败 {failed} 条，示例: {failed_titles[0]}")
            cursor.execute("SELECT COUNT(*) FROM article")
            total = cursor.fetchone()[0]
            print(f"导入完成: 成功 {success} 条, 失败 {failed} 条, 数据库共 {total} 条")
    finally:
        conn.close()


if __name__ == "__main__":
    main()
