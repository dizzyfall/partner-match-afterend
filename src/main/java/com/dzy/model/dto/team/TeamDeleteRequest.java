package com.dzy.model.dto.team;

import lombok.Data;

/**
 * @Author Dzy
 * @Date 2024/2/27  14:09
 */
@Data
public class TeamDeleteRequest {
    /**
     * 队伍唯一标识
     */
    private Long teamId;
}
