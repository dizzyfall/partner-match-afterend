package com.dzy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.domain.User;
import com.dzy.model.dto.user.UserRecommendRequest;
import com.dzy.model.request.UserLoginRequest;
import com.dzy.model.request.UserRegisterRequest;
import com.dzy.model.vo.UserVO;
import com.dzy.service.UserService;
import com.dzy.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.dzy.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @auther DZY
 * @date 2023/5/31 - 10:38
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //用户注册
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR, "注册请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR, "账号，密码，确认密码为空或含有空字符串、空格");
        }
        long userRegister = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResponseUtil.success(StatusCode.REGISTER_SUCCESS, userRegister);
    }

    //用户登录
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR, "登录请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR, "账号，密码为空或含有空字符串、空格");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResponseUtil.success(StatusCode.LOGIN_SUCESS, user);
    }

    //按照用户昵称查询用户(仅管理员)
    @GetMapping("/searchuserbatches")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {
        //鉴权
        boolean userAdmin = userService.isAdmin(request);
        if (!userAdmin) {
            throw new BusinessException(StatusCode.ADMIN_ERROR);
        }
        //todo,分页固定，要封装分页参数
        List<User> userList = userService.userSearchBatches(new Page<>(1, 10), userName);
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS, userList, "用户查询列表");
    }

    //按照用户id删除用户(仅管理员)
    @PostMapping("/deleteuser")
    public BaseResponse<Boolean> deleteUser(@RequestBody long userId, HttpServletRequest request) {
        //鉴权
        boolean userAdmin = userService.isAdmin(request);
        if (!userAdmin) {
            throw new BusinessException(StatusCode.ADMIN_ERROR);
        }
        if (userId < 0) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "无此用户");
        }
        boolean removeId = userService.removeById(userId);
        if (!removeId) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return ResponseUtil.success(StatusCode.DELETE_SUCCESS, "用户删除成功");
    }

    /**
     * 获取用户登录态
     *
     * @param request 请求域
     * @return User
     */
    @GetMapping("/currentuser")
    public BaseResponse<User> getCurrentUserState(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR, "用户参数为空");
        }
        Long userId = currentUser.getUserId();
        // TODO: 2024/1/9  优化用户有其他状态
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResponseUtil.success(StatusCode.STATE_SUCCESS, safetyUser, "获取用户登录态成功");
    }

    /**
     * 用户退出登录
     *
     * @param request 请求域
     * @return 成功退出返回1，否则返回-1
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        int userLogout = userService.userLogout(request);
        if (userLogout == -1) {
            throw new BusinessException(StatusCode.STATE_DELETE_ERROR, "浏览器没有移除用户登录态错误");
        }
        return ResponseUtil.success(StatusCode.LOGOUT_SUCESS, userLogout);
    }

    /**
     * 用户推荐分页查询
     * 使用redis缓存
     *
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> userRecommend(HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //todo,分页固定，要封装分页参数
        Page<User> userRecommendList = userService.userRecommendByPage(new Page<>(1, 10), queryWrapper, request);
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS, userRecommendList, "用户推荐分页查询成功");
    }

    /**
     * 基于编辑距离算法的用户推荐
     *
     * @param userRecommendRequest
     * @param request
     * @return
     */
    @PostMapping("/recommend/user")
    public BaseResponse<List<UserVO>> recommendUser(@RequestBody UserRecommendRequest userRecommendRequest, HttpServletRequest request) {
        if (userRecommendRequest == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        List<UserVO> userRecommendList = userService.userRecommend(userRecommendRequest, loginUser);
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS,userRecommendList,"队伍推荐成功");
    }
}
