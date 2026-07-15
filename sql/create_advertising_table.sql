USE my_article;

-- 运营广告（对应旧 CMS advertising / 轮播运营内容）
CREATE TABLE IF NOT EXISTS advertising
(
    id           bigint auto_increment comment 'id' primary key,
    spaceId      bigint                                not null comment '运营位 id（关联 advertising_space）',
    name         varchar(512)                          not null comment '广告标题',
    cover        varchar(1024)                         null comment '封面图',
    video        varchar(1024)                         null comment '视频地址',
    path         varchar(1024)                         null comment '跳转路径',
    content      longtext                              null comment '详情内容（富文本）',
    remark       varchar(1024)                         null comment '备注/简介（对应旧字段 desc）',
    sort         int         default 0                 not null comment '排序（越大越靠前）',
    status       tinyint     default 1                 not null comment '状态：0-禁用 1-启用',
    startTime    date                                  null comment '开始展示日期',
    endTime      date                                  null comment '结束展示日期',
    createUserId bigint                                null comment '创建人 id',
    createTime   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint     default 0                 not null comment '是否删除',
    index idx_spaceId (spaceId),
    index idx_status (status),
    index idx_sort (sort),
    index idx_startTime (startTime),
    index idx_endTime (endTime)
) comment '运营广告' collate = utf8mb4_unicode_ci;
