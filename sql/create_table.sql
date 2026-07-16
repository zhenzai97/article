# 数据库初始化（本项目唯一建表 SQL 文件）
#
# 【约定】所有新建 / 变更表结构的 DDL 只追加到本文件，禁止再新增 sql/create_xxx_table.sql。
# 种子数据可用 sql/seed_*.sql；ES 映射等非 MySQL DDL 可单独存放。
# 详见 docs/project-conventions.md

-- 创建库
create database if not exists my_article;

-- 切换库
use my_article;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;

-- 角色表
create table if not exists role
(
    id           bigint auto_increment comment 'id' primary key,
    roleCode     varchar(64)                           not null comment '角色编码',
    roleName     varchar(128)                          not null comment '角色名称',
    roleDesc     varchar(512)                          null comment '角色描述',
    status       tinyint     default 1                 not null comment '状态：0-禁用 1-启用',
    sort         int         default 0                 not null comment '排序',
    isBuiltin    tinyint     default 0                 not null comment '是否内置：0-否 1-是',
    isSuperAdmin tinyint     default 0                 not null comment '是否超级管理员：0-否 1-是',
    dataScope    tinyint     default 1                 null comment '数据范围：1-全部 2-本部门 3-仅本人',
    createUserId bigint                                null comment '创建人id',
    createTime   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint     default 0                 not null comment '是否删除',
    unique key uk_roleCode (roleCode)
) comment '角色' collate = utf8mb4_unicode_ci;

-- 菜单表
create table if not exists menu
(
    id         bigint auto_increment comment 'id' primary key,
    parentId   bigint      default 0                 not null comment '父菜单id，0表示主类目/根节点',
    menuName   varchar(128)                          not null comment '菜单名称',
    menuType   tinyint                               not null comment '菜单类型：1-目录 2-菜单 3-按钮',
    icon       varchar(128)                          null comment '菜单图标',
    sort       int         default 0                 not null comment '显示排序',
    path       varchar(256)                          null comment '路由地址',
    component  varchar(256)                          null comment '组件路径',
    permCode   varchar(128)                          null comment '权限字符，如 system:user:list',
    remark     varchar(512)                          null comment '备注',
    visible    tinyint     default 1                 not null comment '显示状态：0-隐藏 1-显示',
    status     tinyint     default 1                 not null comment '菜单状态：0-停用 1-正常',
    isCache    tinyint     default 0                 not null comment '是否缓存：0-不缓存 1-缓存',
    createUserId bigint                                null comment '创建人id',
    createTime datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint     default 0                 not null comment '是否删除',
    index idx_parentId (parentId)
) comment '菜单' collate = utf8mb4_unicode_ci;

-- 角色菜单关联表（硬删除；超级管理员角色不写此表，代码返回全部菜单）
create table if not exists role_menu
(
    id           bigint auto_increment comment 'id' primary key,
    roleId       bigint                             not null comment '角色 id',
    menuId       bigint                             not null comment '菜单 id',
    createUserId bigint                             null comment '分配人 id',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '分配时间',
    unique key uk_role_menu (roleId, menuId),
    index idx_roleId (roleId),
    index idx_menuId (menuId)
) comment '角色菜单关联';

-- 文章分类表
create table if not exists article_category
(
    id           bigint auto_increment comment 'id' primary key,
    sign         varchar(64)                           not null comment '分类标识',
    name         varchar(128)                          not null comment '分类名称',
    sort         int         default 0                 not null comment '排序',
    status       tinyint     default 1                 not null comment '状态：0-禁用 1-启用',
    remark       varchar(512)                          null comment '备注',
    createUserId bigint                                null comment '创建人id',
    createTime   datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint     default 0                 not null comment '是否删除',
    unique key uk_sign (sign)
) comment '文章分类' collate = utf8mb4_unicode_ci;

-- 文章表
create table if not exists article
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

-- 运营广告位表
-- JSON 字段映射：sign、name、status、desc→remark；丢弃 _id；_createTime/_updateTime→createTime/updateTime
create table if not exists advertising_space
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

-- 运营广告（对应旧 CMS advertising / 轮播运营内容）
create table if not exists advertising
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

-- 文旅内容展示表（详见 create_tourism_content.sql）
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

-- 首页区块配置详见 create_home_section.sql

-- -- 帖子表
-- create table if not exists post
-- (
--     id         bigint auto_increment comment 'id' primary key,
--     title      varchar(512)                       null comment '标题',
--     content    text                               null comment '内容',
--     tags       varchar(1024)                      null comment '标签列表（json 数组）',
--     thumbNum   int      default 0                 not null comment '点赞数',
--     favourNum  int      default 0                 not null comment '收藏数',
--     userId     bigint                             not null comment '创建用户 id',
--     createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     isDelete   tinyint  default 0                 not null comment '是否删除',
--     index idx_userId (userId)
-- ) comment '帖子' collate = utf8mb4_unicode_ci;
--
-- -- 帖子点赞表（硬删除）
-- create table if not exists post_thumb
-- (
--     id         bigint auto_increment comment 'id' primary key,
--     postId     bigint                             not null comment '帖子 id',
--     userId     bigint                             not null comment '创建用户 id',
--     createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     index idx_postId (postId),
--     index idx_userId (userId)
-- ) comment '帖子点赞';
--
-- -- 帖子收藏表（硬删除）
-- create table if not exists post_favour
-- (
--     id         bigint auto_increment comment 'id' primary key,
--     postId     bigint                             not null comment '帖子 id',
--     userId     bigint                             not null comment '创建用户 id',
--     createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     index idx_postId (postId),
--     index idx_userId (userId)
-- ) comment '帖子收藏';

