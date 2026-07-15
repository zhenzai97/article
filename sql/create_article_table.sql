USE my_article;

CREATE TABLE IF NOT EXISTS article
(
    id           bigint auto_increment comment 'id' primary key,
    categoryId   bigint                                not null comment '分类 id',
    title        varchar(512)                          not null comment '标题',
    content      longtext                              null comment '内容',
    cover        varchar(1024)                         null comment '封面图',
    video        varchar(1024)                         null comment '视频地址',
    sort         int         default 0                 not null comment '排序',
    readNum      int         default 0                 not null comment '阅读量',
    status       tinyint     default 1                 not null comment '状态：0-禁用 1-启用',
    isHome       tinyint     default 0                 not null comment '是否首页展示：0-否 1-是',
    isTop        tinyint     default 0                 not null comment '是否置顶：0-否 1-是',
    createUserId bigint                                null comment '创建人 id',
    createTime   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint     default 0                 not null comment '是否删除',
    index idx_categoryId (categoryId),
    index idx_status (status),
    index idx_isHome (isHome),
    index idx_isTop (isTop)
) comment '文章' collate = utf8mb4_unicode_ci;
