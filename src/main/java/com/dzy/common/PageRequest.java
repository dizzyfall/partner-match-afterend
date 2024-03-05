package com.dzy.common;

import lombok.Data;

/**
 * @Author Dzy
 * @Date 2024/2/27  16:17
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageCurrent = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;
}
