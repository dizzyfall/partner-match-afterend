package com.dzy.model.dto.team;

import lombok.Data;

/**
 * 用户加入的队伍
 * @Author Dzy
 * @Date 2024/3/4  17:28
 */
@Data
public class TeamMyJoinAndCreateRequest {
    /**
     * 队伍用户id
     */
    private Long teamUserId;
}
