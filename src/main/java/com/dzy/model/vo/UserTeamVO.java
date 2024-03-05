package com.dzy.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户队伍响应值封装
 *
 * @Author Dzy
 * @Date 2024/3/2  18:12
 */
@Data
public class UserTeamVO implements Serializable {

    private static final long serialVersionUID = 2720249060497127760L;
    /**
     * 队伍唯一标识
     */
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
     * 加入队伍用户列表
     */
    private UserVO createUser;

}
