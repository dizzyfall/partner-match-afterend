package com.dzy.constant;

/**
 * 队伍状态枚举类
 *
 * @Author Dzy
 * @Date 2024/2/28  11:22
 */
public enum TeamStatus {

    PUBLIC(0, "公开"),

    PRIVATE(1, "私密"),

    ENCRYPT(2, "加密");

    private final Integer status;

    private final String value;

    TeamStatus(Integer status, String value) {
        this.status = status;
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }

    public static TeamStatus getTeamStatusEnumByKey(Integer status) {
        if (status == null) {
            return null;
        }
        TeamStatus[] values = TeamStatus.values();
        for (TeamStatus teamStatus : values) {
            if (teamStatus.getStatus().equals(status)) {
                return teamStatus;
            }
        }
        return null;
    }
}
