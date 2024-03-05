package com.dzy.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍
 *
 * @TableName team
 */
@TableName(value = "team")
@Data
@Component
public class Team implements Serializable {
    /**
     * 队伍唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Long teamId;

    /**
     * 队伍昵称
     */
    private String teamName;

    /**
     * 队伍介绍
     */
    private String teamDescription;

    /**
     * 队伍密码
     */
    private String teamPassword;

    /**
     * 队伍最大人数
     */
    private Integer teamMaxNum;

    /**
     * 队伍状态 0:公开 1:私密 2:加密
     */
    private Integer teamStatus;

    /**
     * 队伍用户id
     */
    private Long teamUserId;

    /**
     * 队伍过期时间
     */
    private Date teamExpireTime;

    /**
     * 队伍数据创建时间
     */
    private Date createTime;

    /**
     * 队伍更新时间
     */
    private Date updateTime;

    /**
     * 队伍数据是否删除 0:未删除 1:已删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}