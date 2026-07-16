-- 文旅内容展示表（农产品/品牌/市场/珠宝玉石/美食/景点）
-- type: product | brand | market | jewelry | food | scenic
create table if not exists tourism_content
(
    id           bigint auto_increment comment 'id' primary key,
    type         varchar(32)                            not null comment '类型：product/brand/market/jewelry/food/scenic',
    name         varchar(256)                           not null comment '名称',
    cover        varchar(1024)                          null comment '封面图',
    intro        varchar(1024)                          null comment '简介（旧 intro/desc 合并）',
    content      longtext                               null comment '详情富文本',
    sort         int          default 0                 not null comment '排序（越大越靠前）',
    status       tinyint      default 1                 not null comment '状态：0-禁用 1-启用',
    isRecommend  tinyint      default 0                 not null comment '是否推荐：0-否 1-是（品牌/市场）',
    readCount    int          default 0                 not null comment '阅读量',
    album        json                                   null comment '相册图片 JSON 数组（美食/景点）',
    address      varchar(512)                           null comment '地址（景点）',
    coordinate   varchar(64)                            null comment '坐标 lat,lng（景点）',
    mobile       varchar(64)                            null comment '联系电话（景点）',
    openTime     varchar(128)                           null comment '开放时间（景点）',
    ticketPrice  varchar(64)                            null comment '门票价格（景点）',
    sourceId     varchar(64)                            null comment '旧 CMS _id，便于幂等导入',
    createUserId bigint                                 null comment '创建人 id',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    unique key uk_sourceId (sourceId),
    index idx_type (type),
    index idx_status (status),
    index idx_sort (sort),
    index idx_isRecommend (isRecommend)
) comment '文旅内容展示' collate = utf8mb4_unicode_ci;
