-- 文章分类初始数据（由旧系统 JSON 迁移，已剔除 _id、pid、_createTime、_updateTime 等无用字段）
USE my_article;

INSERT INTO article_category (sign, name, sort, status, createTime, updateTime)
VALUES
    ('xhdt',        '协会动态',           0, 1, FROM_UNIXTIME(1713157185493 / 1000), FROM_UNIXTIME(1713333088911 / 1000)),
    ('wxyw',        '网信要闻',           0, 1, FROM_UNIXTIME(1713157203370 / 1000), FROM_UNIXTIME(1713333097592 / 1000)),
    ('dhnews',      '精彩德宏新闻资讯',   0, 1, FROM_UNIXTIME(1713333271487 / 1000), FROM_UNIXTIME(1714287111898 / 1000)),
    ('dhFunNews',   '精彩德宏好玩资讯',   0, 1, FROM_UNIXTIME(1713333398787 / 1000), FROM_UNIXTIME(1714287084339 / 1000)),
    ('dhFoodNews',  '精彩德宏好吃资讯',   0, 1, FROM_UNIXTIME(1713333431112 / 1000), FROM_UNIXTIME(1714287096824 / 1000)),
    ('jadeNews',    '珠宝玉石行业动态',   0, 1, FROM_UNIXTIME(1715147430747 / 1000), FROM_UNIXTIME(1715147430747 / 1000)),
    ('produceNews', '农产品行业动态',     0, 1, FROM_UNIXTIME(1715147614547 / 1000), FROM_UNIXTIME(1715147614547 / 1000)),
    ('zwhdt',       '专委会动态',         0, 1, FROM_UNIXTIME(1734536426040 / 1000), FROM_UNIXTIME(1734536453332 / 1000))
ON DUPLICATE KEY UPDATE
    name       = VALUES(name),
    sort       = VALUES(sort),
    status     = VALUES(status),
    updateTime = VALUES(updateTime);
