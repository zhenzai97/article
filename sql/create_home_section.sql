-- 首页区块配置（方案 B）
create table if not exists home_section
(
    id            bigint auto_increment comment 'id' primary key,
    code          varchar(64)                            not null comment '区块编码：carousel/channel/banner/special/article_home/article_cat/activity',
    title         varchar(128)                           null comment '展示标题',
    visible       tinyint      default 1                 not null comment '是否显示：0-否 1-是',
    sort          int          default 0                 not null comment '排序（越大越靠前）',
    limitNum      int          default 5                 not null comment '拉取条数',
    spaceSign     varchar(64)                            null comment '广告运营位 sign',
    categorySign  varchar(64)                            null comment '文章栏目 sign',
    status        tinyint      default 1                 not null comment '状态：0-禁用 1-启用',
    createUserId  bigint                                 null comment '创建人 id',
    createTime    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint      default 0                 not null comment '是否删除',
    unique key uk_code (code),
    index idx_sort (sort),
    index idx_status (status)
) comment '首页区块配置' collate = utf8mb4_unicode_ci;

-- 默认区块（对齐现有小程序首页）
INSERT INTO home_section (code, title, visible, sort, limitNum, spaceSign, categorySign, status)
VALUES
    ('carousel',     '首页轮播',   1, 100, 6, 'focus',   NULL,     1),
    ('channel',      '宫格频道',   1, 90,  6, 'channel', NULL,     1),
    ('banner',       '通栏',       1, 80,  1, 'banner',  NULL,     1),
    ('special',      '专题',       1, 70,  2, 'special', NULL,     1),
    ('article_home', '首页推荐',   1, 60,  5, NULL,      NULL,     1),
    ('article_dhnews','精彩德宏',  1, 50,  2, NULL,      'dhnews', 1),
    ('article_xhdt', '协会动态',   1, 40,  2, NULL,      'xhdt',   1),
    ('activity',     '活动一览',   1, 30,  2, NULL,      NULL,     1)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    visible = VALUES(visible),
    sort = VALUES(sort),
    limitNum = VALUES(limitNum),
    spaceSign = VALUES(spaceSign),
    categorySign = VALUES(categorySign),
    status = VALUES(status);
