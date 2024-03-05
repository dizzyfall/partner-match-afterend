package com.dzy.constant;

/**
 * 用户常量
 *
 * @auther DZY
 * @date 2023/6/1 - 22:31
 */
public interface UserConstant {
    /**
     * 用户登录态
     */
    String USER_LOGIN_STATE = "userLoginState";

    //权限
    /**
     * 普通用户
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员
     */
    int ADMIN_ROLE = 1;
}
