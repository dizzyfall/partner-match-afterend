-- auto-generated definition
create table user_team
(
    userteamId bigint auto_increment comment '用户队伍关系唯一标识'
        primary key,
    userId     bigint                             null comment '用户id',
    teamId     bigint                             null comment '队伍id',
    joinTime   datetime                           null comment '加入时间',
    createTime datetime default CURRENT_TIMESTAMP not null comment '数据创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '数据是否删除 0:未删除 1:已删除'
)
    comment '用户队伍关系表';