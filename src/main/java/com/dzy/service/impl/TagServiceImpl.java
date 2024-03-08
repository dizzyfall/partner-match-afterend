package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.model.domain.Tag;
import com.dzy.service.TagService;
import com.dzy.mapper.TagMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【tag(标签)】的数据库操作Service实现
 * @createDate 2024-02-07 11:35:27
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    /**
     * 将标签json字符串转为java对象列表
     *
     * @param tags 标签json字符串
     * @return 标签java对象列表
     */
    @Override
    public List<String> convertTagsToTagNameList(String tags) {
        //json字符串转java对象列表
        Gson gson = new Gson();
        return gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
    }
}




