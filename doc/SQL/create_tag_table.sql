-- auto-generated definition
create table tag
(
    tagId      bigint auto_increment comment '标签唯一标识'
        primary key,
    userId     bigint                             null comment '用户id',
    parentId   bigint                             null comment '父标签id',
    tagName    varchar(256)                       null comment '用户昵称',
    isParent   tinyint                            null comment '是否为父标签
0:不是
1:是',
    createTime datetime default CURRENT_TIMESTAMP not null comment '标签创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '标签更新时间',
    isDelete   tinyint  default 0                 not null comment '标签是否删除
0:未删除
1:已删除',
    constraint uniIdx_tagName
        unique (tagName)
)
    comment '标签';

create index idx_userId
    on tag (userId);

