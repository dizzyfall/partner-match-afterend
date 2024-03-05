-- auto-generated definition
create table user
(
    userId       bigint auto_increment comment '用户唯一标识'
        primary key,
    userName     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '用户账号',
    userPassword varchar(512)                       not null comment '用户密码',
    gender       varchar(32)                        null comment '用户性别',
    birthday     date                               null comment '用户生日',
    phone        varchar(128)                       null comment '用户电话号码',
    email        varchar(128)                       null comment '用户邮箱',
    avatarUrl    varchar(1024)                      null comment '用户头像路径',
    userRole     int      default 0                 not null comment '用户权限
0:普通用户
1:管理员',
    userStatus   tinyint  default 0                 not null comment '用户状态',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '用户数据创建时间',
    isDelete     tinyint  default 0                 not null comment '用户数据是否删除
0:未删除
1:已删除',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '用户更新时间',
    tags         varchar(1024)                      null comment '标签列表'
)
    comment '用户';