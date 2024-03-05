package com.dzy.constant;

/**
 * 自定义响应状态类
 *
 * @auther DZY
 * @date 2024/1/11 - 15:24
 */
public enum StatusCode {
    LOGIN_SUCESS("20001", "登录成功", ""),
    REGISTER_SUCCESS("20002", "注册成功", ""),
    SEARCH_SUCCESS("20003", "查询成功", ""),
    STATE_SUCCESS("20004", "获取状态成功", ""),
    DELETE_SUCCESS("20005", "删除成功", ""),
    LOGOUT_SUCESS("20006", "退出登录成功", ""),
    ADD_SUCESS("20007", "添加成功", ""),
    UPDATE_SUCESS("20008", "更新成功", ""),


    DATA_NULL_ERROR("40001", "请求数据为空", ""),
    PARAM_ERROR("40002", "请求参数错误", ""),
    PARAM_NULL_ERROR("40003", "请求参数为空", ""),
    STATE_DELETE_ERROR("40004", "删除状态失败", ""),

    NO_LOGIN_ERROR("40100", "未登录错误", ""),
    ADMIN_ERROR("40101", "无权限", ""),

    SYSTEM_ERROR("50000", "服务器错误", ""),
    DATABASE_ERROR("50001", "数据库错误", "");


    /**
     * 状态码
     */
    private final String code;

    /**
     * 响应信息
     */
    private final String msg;

    /**
     * 固定响应详情
     */
    private final String description;

    StatusCode(String code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDesciption() {
        return description;
    }
}
