USE my_article;

CREATE TABLE IF NOT EXISTS advertising_space
(
    id           bigint auto_increment comment 'id' primary key,
    sign         varchar(64)                           not null comment '广告位标识',
    name         varchar(128)                          not null comment '广告位名称',
    remark       varchar(512)                          null comment '备注（对应旧字段 desc）',
    status       tinyint     default 1                 not null comment '状态：0-禁用 1-启用',
    createUserId bigint                                null comment '创建人id',
    createTime   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint     default 0                 not null comment '是否删除',
    unique key uk_sign (sign)
) comment '运营广告位' collate = utf8mb4_unicode_ci;
