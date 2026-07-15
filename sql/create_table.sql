# 数据库初始化

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

