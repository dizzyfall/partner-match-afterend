package com.dzy.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户响应值封装类
 *
 * @Author Dzy
 * @Date 2024/3/2  18:26
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -905205232856283858L;
    /**
     * 用户唯一标识
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户生日
     */
    private LocalDate birthday;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户头像路径
     */
    private String avatarUrl;

    /**
     * 用户状态
     */
    private Integer userStatus;

    /**
     * 用户权限
     * 0:普通用户
     * 1:管理员
     */
    private Integer userRole;

    /**
     * 用户数据创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 标签列表就json
     */
    private String tags;
}
