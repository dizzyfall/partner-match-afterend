package com.dzy.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 标签
 *
 * @TableName tag
 */
@TableName(value = "tag")
@Data
@Component
public class Tag implements Serializable {
    /**
     * 标签唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Long tagId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 父标签id
     */
    private Long parentId;

    /**
     * 用户昵称
     */
    private String tagName;

    /**
     * 是否为父标签
     * 0:不是
     * 1:是
     */
    private Integer isParent;

    /**
     * 标签创建时间
     */
    private Date createTime;

    /**
     * 标签更新时间
     */
    private Date updateTime;

    /**
     * 标签是否删除
     * 0:未删除
     * 1:已删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}