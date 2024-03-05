package com.dzy.service;

import com.dzy.model.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【tag(标签)】的数据库操作Service
 * @createDate 2024-02-07 11:35:27
 */
public interface TagService extends IService<Tag> {
    /**
     * 将标签json字符串转为java对象列表
     *
     * @param tags 标签json字符串
     * @return 标签java对象列表
     */
    List<String> convertTagsToTagNameList(String tags);
}
