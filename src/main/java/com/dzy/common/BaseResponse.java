package com.dzy.common;

import com.dzy.constant.StatusCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @auther DZY
 * @date 2023/5/25 - 15:33
 */
@Data
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private final String code;

    /**
     * 返回信息
     */
    private final String msg;

    /**
     * 自定义响应详情
     */
    private final String description;

    /**
     * 返回对象
     */
    private T data;

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = statusCode.getDesciption();
    }

    public BaseResponse(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = statusCode.getDesciption();
        this.data = data;
    }

    public BaseResponse(StatusCode statusCode, T data, String description) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = description;
        this.data = data;
    }

    public BaseResponse(StatusCode statusCode, String description) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.description = description;
    }

    public BaseResponse(String code, String msg, String description, T data) {
        this.code = code;
        this.msg = msg;
        this.description = description;
        this.data = data;
    }

    public BaseResponse(String code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }
}
