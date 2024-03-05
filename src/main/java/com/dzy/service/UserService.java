package com.dzy.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author DZY
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-05-24 21:15:49
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户第一次密码
     * @param checkPassword 用户第二次密码
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 用户对象
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 按照用户昵称查询用户
     *
     * @param userName 用户名
     * @return 用户列表
     */
    List<User> userSearchBatches(IPage<User> page, String userName);

    /**
     * 用户脱敏
     *
     * @param loginUser 数据库User对象
     * @return 脱敏后User
     */
    User getSafetyUser(User loginUser);

    /**
     * 是否为管理员
     *
     * @param request 请求域
     * @return boolean
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * 方法重载
     *
     * @param user 用户
     * @return boolean
     */
    boolean isAdmin(User user);

    /**
     * 用户退出登录
     *
     * @param request
     * @return 成功退出返回1，否则返回-1
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签查询用户
     *
     * @param tagNameList 标签列表json
     * @return 用户列表
     */
    List<User> userSearchByAllTags(List<String> tagNameList);

    /**
     * 获取用户登录态
     *
     * @param request
     * @return
     */
    User getUserLoginState(HttpServletRequest request);

    /**
     * 用户更新信息
     *
     * @param user      前端请求参数封装
     * @param loginUser 当前用户登录态
     * @return int
     */
    int userUpdate(User user, User loginUser);

    /**
     * 推荐用户
     * 分页
     *
     * @param page         分页参数
     * @param queryWrapper 条件查询器
     * @return Page<User>
     */
    Page<User> userRecommendByPage(IPage<User> page, Wrapper<User> queryWrapper, HttpServletRequest request);
}
