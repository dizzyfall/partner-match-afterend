package com.dzy.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author DZY
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2023-05-24 21:15:49
 * @Entity generator.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * @param page
     * @return
     */
    Page<User> userRecommendByPage(IPage<User> page, Wrapper<User> queryWrapper);
}




