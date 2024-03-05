package com.dzy.model.dto.team;

import com.dzy.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Dzy
 * @Date 2024/2/27  14:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQueryRequest extends PageRequest {
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
     * 搜索词
     */
    private String teamSearchText;

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
}
