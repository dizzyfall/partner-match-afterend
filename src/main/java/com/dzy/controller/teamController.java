package com.dzy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.common.BaseResponse;
import com.dzy.constant.StatusCode;
import com.dzy.exception.BusinessException;
import com.dzy.model.domain.Team;
import com.dzy.model.domain.User;
import com.dzy.model.dto.team.*;
import com.dzy.service.TeamService;
import com.dzy.service.UserService;
import com.dzy.service.UserTeamService;
import com.dzy.util.ResponseUtil;
import com.dzy.model.vo.UserTeamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @auther DZY
 * @date 2023/5/31 - 10:38
 */
@RestController
@RequestMapping("/team")
public class teamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTeamService userTeamService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        // true passed to CustomDateEditor constructor means convert empty String to null
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        long teamAdd = teamService.teamAdd(teamAddRequest, loginUser);
        if (teamAdd < 0) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return ResponseUtil.success(StatusCode.ADD_SUCESS, teamAdd);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        if (teamDeleteRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        boolean teamDelete = teamService.teamDelete(teamDeleteRequest, loginUser);
        if (!teamDelete) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return ResponseUtil.success(StatusCode.DELETE_SUCCESS, "队伍删除成功");
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        boolean teamUpdate = teamService.teamUpdate(teamUpdateRequest, loginUser);
        if (!teamUpdate) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return ResponseUtil.success(StatusCode.UPDATE_SUCESS, "队伍更新成功");
    }

    @GetMapping("/list/team_all")
    public BaseResponse<List<Team>> searchTeamAll() {
        List<Team> teamList = teamService.teamSearchAll();
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS, teamList);
    }

    @GetMapping("/list/team_specific")
    public BaseResponse<List<UserTeamVO>> searchTeamBySpecificField(TeamQueryRequest teamQueryRequest, HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User user = userService.getUserLoginState(request);
        List<UserTeamVO> teamList = teamService.teamSearchBySpecificField(teamQueryRequest, user);
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS, teamList, "队伍查询成功");
    }

    @GetMapping("/list/team_page")
    public BaseResponse<List<Team>> searchTeamByPage(TeamQueryRequest teamQueryRequest) {
        if (teamQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        Page<Team> teamPage = teamService.teamSearchByPage(teamQueryRequest);
        List<Team> teamList = teamPage.getRecords();
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS, teamList, "队伍更查询成功");
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        Boolean res = teamService.teamJoin(teamJoinRequest, loginUser);
        return ResponseUtil.success(StatusCode.ADD_SUCESS, "用户加入队伍成功");
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        Boolean res = teamService.teamQuit(teamQuitRequest, loginUser);
        return ResponseUtil.success(StatusCode.DELETE_SUCCESS, "用户退出队伍成功");
    }

    @PostMapping("/my/join")
    public BaseResponse<List<UserTeamVO>> myJoinedTeam(@RequestBody TeamMyJoinAndCreateRequest teamMyJoinAndCreateRequest, HttpServletRequest request) {
        if (teamMyJoinAndCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        List<UserTeamVO> myJoinTeamList = teamService.teamMyJoin(teamMyJoinAndCreateRequest,loginUser);
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS,myJoinTeamList,"查询用户加入的队伍成功");
    }

    /**
     * 查询用户创建的队伍
     *
     * @param teamMyJoinAndCreateRequest
     * @param request
     * @return
     */
    @PostMapping("/my/create")
    public BaseResponse<List<UserTeamVO>> myCreatedTeam(@RequestBody  TeamMyJoinAndCreateRequest teamMyJoinAndCreateRequest, HttpServletRequest request) {
        if (teamMyJoinAndCreateRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (request == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        User loginUser = userService.getUserLoginState(request);
        List<UserTeamVO> myCreateTeamList= teamService.teamMyCreate(teamMyJoinAndCreateRequest, loginUser);
        return ResponseUtil.success(StatusCode.SEARCH_SUCCESS,myCreateTeamList, "查询用户创建的队伍成功");
    }

}
