-- auto-generated definition
create table team
(
    teamId          bigint auto_increment comment '队伍唯一标识'
        primary key,
    teamName        varchar(256)                       not null comment '队伍昵称',
    teamDescription varchar(1024)                      null comment '队伍介绍',
    teamPassword    varchar(512)                       null comment '队伍密码',
    teamMaxNum      tinyint  default 1                 not null comment '队伍最大人数',
    teamStatus      tinyint  default 0                 not null comment '队伍状态 0:公开 1:私密 2:加密',
    teamUserId      bigint comment '队伍用户id',
    teamExpireTime  datetime                           null comment '队伍过期时间',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '队伍数据创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '队伍更新时间',
    isDelete        tinyint  default 0                 not null comment '队伍数据是否删除 0:未删除 1:已删除'
)
    comment '队伍';