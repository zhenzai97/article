-- 运营广告位初始数据（由旧 CMS JSON 迁移）
-- desc → remark；status true → 1；丢弃 _id
USE my_article;

INSERT INTO advertising_space (sign, name, remark, status, createTime, updateTime)
VALUES
    ('focus',          '首页轮播',               '最多显示6个',             1, FROM_UNIXTIME(1713155650342 / 1000), FROM_UNIXTIME(1713156764314 / 1000)),
    ('channel',        '首页宫格频道',           '显示6个',                 1, FROM_UNIXTIME(1713155682024 / 1000), FROM_UNIXTIME(1713172016612 / 1000)),
    ('banner',         '首页通栏频道',           '只显示一个',               1, FROM_UNIXTIME(1713156661524 / 1000), FROM_UNIXTIME(1713172023280 / 1000)),
    ('special',        '首页专题频道',           '显示一行，2个一行',         1, FROM_UNIXTIME(1713156696608 / 1000), FROM_UNIXTIME(1713172028785 / 1000)),
    ('fun',            '精彩德宏—好玩',           '精彩德宏好玩频道轮播',     1, FROM_UNIXTIME(1713170253356 / 1000), FROM_UNIXTIME(1713170300139 / 1000)),
    ('delicious',      '精彩德宏—好吃',           '精彩德宏好玩频道轮播',     1, FROM_UNIXTIME(1713170286530 / 1000), FROM_UNIXTIME(1713171981315 / 1000)),
    ('farmProduce',    '特色产业-农产品',         '特色产品农产品轮播',       1, FROM_UNIXTIME(1713171667396 / 1000), FROM_UNIXTIME(1713171881210 / 1000)),
    ('jadeite',        '特色产业-珠宝翡翠',       '特色产业珠宝翡翠轮播',     1, FROM_UNIXTIME(1713171873370 / 1000), FROM_UNIXTIME(1713171873370 / 1000)),
    ('jadeiteSpecial', '特色产业-珠宝翡翠-专题', '特色产业珠宝翡翠专题',     1, FROM_UNIXTIME(1713172108073 / 1000), FROM_UNIXTIME(1713172108073 / 1000)),
    ('fzc_carousel',   '非洲翠产业联盟轮播图',   '非洲翠产业联盟轮播图',     1, FROM_UNIXTIME(1734702537709 / 1000), FROM_UNIXTIME(1734702551310 / 1000))
ON DUPLICATE KEY UPDATE
    name       = VALUES(name),
    remark     = VALUES(remark),
    status     = VALUES(status),
    updateTime = VALUES(updateTime);
