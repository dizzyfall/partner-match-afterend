package com.dzy.util;

import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;

import java.io.Serializable;

/**
 * 创建返回对象，不用每次new一个BaseReponse对象
 *
 * @auther DZY
 * @date 2024/1/11 - 15:23
 */
public class ResponseUtil<T> implements Serializable {

    public static <T> BaseResponse<T> success(StatusCode statusCode, String description) {
        return new BaseResponse<>(statusCode, description);
    }

    public static <T> BaseResponse<T> success(StatusCode statusCode, T data) {
        return new BaseResponse<>(statusCode, data);
    }

    public static <T> BaseResponse<T> success(StatusCode statusCode, T data, String description) {
        return new BaseResponse<>(statusCode, data, description);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode) {
        return new BaseResponse<>(statusCode);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode, T data) {
        return new BaseResponse<>(statusCode, data);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode, T data, String description) {
        return new BaseResponse<>(statusCode, data, description);
    }

    public static <T> BaseResponse<T> error(StatusCode statusCode, String description) {
        return new BaseResponse<>(statusCode, description);
    }

    public static <T> BaseResponse<T> error(String code, String msg, String description) {
        return new BaseResponse<>(code, msg, description);
    }
}
