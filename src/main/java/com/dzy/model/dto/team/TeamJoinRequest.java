package com.dzy.model.dto.team;

import lombok.Data;

/**
 * @Author Dzy
 * @Date 2024/2/27  14:03
 */
@Data
public class TeamJoinRequest {
    /**
     * 队伍唯一标识
     */
    private Long teamId;

    /**
     * 队伍密码
     */
    private String teamPassword;
}
