-- 应聘表
-- 旧 CMS：candidate_phone→mobile、company_id→companyId/companyName、
-- jop_id（岗位，拼写错误）→recruitmentId/recruitmentName、salary_range→salaryRange、_id→sourceId
create table if not exists applicant
(
    id               bigint auto_increment comment 'id' primary key,
    name             varchar(128)                           not null comment '应聘人姓名',
    mobile           varchar(64)                            null comment '联系电话（旧 candidate_phone）',
    salaryRange      varchar(64)                            null comment '期望薪资范围（如 5000-10000）',
    companyId        bigint                                 null comment '公司 id（关联 company）',
    companyName      varchar(256)                           null comment '公司名称（冗余展示）',
    recruitmentId    bigint                                 null comment '招聘岗位 id（关联 recruitment，旧 jop_id）',
    recruitmentName  varchar(256)                           null comment '岗位名称（冗余展示）',
    sourceId         varchar(64)                            null comment '旧 CMS _id，便于幂等导入',
    createUserId     bigint                                 null comment '创建人 id',
    createTime       datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint      default 0                 not null comment '是否删除',
    unique key uk_sourceId (sourceId),
    index idx_companyId (companyId),
    index idx_recruitmentId (recruitmentId),
    index idx_name (name),
    index idx_mobile (mobile)
) comment '应聘' collate = utf8mb4_unicode_ci;
