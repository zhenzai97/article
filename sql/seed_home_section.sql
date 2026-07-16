-- 首页区块默认配置（对齐现有小程序首页）
-- 执行前请先执行 create_table.sql 中的 home_section 建表语句
USE my_article;

INSERT INTO home_section (code, title, visible, sort, limitNum, spaceSign, categorySign, status)
VALUES
    ('carousel',      '首页轮播',   1, 100, 6, 'focus',   NULL,     1),
    ('channel',       '宫格频道',   1, 90,  6, 'channel', NULL,     1),
    ('banner',        '通栏',       1, 80,  1, 'banner',  NULL,     1),
    ('special',       '专题',       1, 70,  2, 'special', NULL,     1),
    ('article_home',  '首页推荐',   1, 60,  5, NULL,      NULL,     1),
    ('article_dhnews','精彩德宏',   1, 50,  2, NULL,      'dhnews', 1),
    ('article_xhdt',  '协会动态',   1, 40,  2, NULL,      'xhdt',   1),
    ('activity',      '活动一览',   1, 30,  2, NULL,      NULL,     1)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    visible = VALUES(visible),
    sort = VALUES(sort),
    limitNum = VALUES(limitNum),
    spaceSign = VALUES(spaceSign),
    categorySign = VALUES(categorySign),
    status = VALUES(status);
