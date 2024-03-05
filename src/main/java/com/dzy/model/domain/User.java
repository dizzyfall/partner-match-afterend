package com.dzy.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
@Component
public class User implements Serializable {

    /**
     * 用户唯一标识
     */
    @TableId(type = IdType.AUTO)
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
     * 用户密码
     */
    private String userPassword;

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
     * 用户数据是否删除
     * 0:未删除
     * 1:已删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 标签列表就json
     */
    private String tags;
}