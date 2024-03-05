package com.dzy.model.dto.team;

import lombok.Data;

import java.util.Date;

/**
 * @Author Dzy
 * @Date 2024/2/27  14:03
 */
@Data
public class TeamUpdateRequest {
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
}
