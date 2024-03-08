package com.dzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dzy.model.domain.Team;
import com.dzy.model.domain.User;
import com.dzy.model.dto.team.*;
import com.dzy.model.vo.UserTeamVO;

import java.util.List;

/**
 * @author DZY
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2024-02-26 20:54:58
 */
public interface TeamService extends IService<Team> {

    /**
     * 添加队伍
     *
     * @param teamAddRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 队伍id
     */
    long teamAdd(TeamAddRequest teamAddRequest, User loginUser);

    /**
     * 删除队伍
     *
     * @param teamDeleteRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 是否成功删除
     */
    boolean teamDelete(TeamDeleteRequest teamDeleteRequest, User loginUser);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 是否成功更新
     */
    boolean teamUpdate(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 查询所有队伍
     *
     * @return 队伍列表
     */
    List<Team> teamSearchAll();

    /**
     * 指定条件查询队伍
     *
     * @param teamQueryRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 队伍列表
     */
    List<UserTeamVO> teamSearchBySpecificField(TeamQueryRequest teamQueryRequest, User loginUser);

    /**
     * 分页查询队伍
     *
     * @param teamQueryRequest 队伍请求参数封装对象
     * @return 分页队伍
     */
    Page<Team> teamSearchByPage(TeamQueryRequest teamQueryRequest);

    /**
     * 根据队伍id查询队伍
     *
     * @param teamId 队伍id
     * @return 队伍
     */
    Team teamSearchById(long teamId);

    Boolean teamJoin(TeamJoinRequest teamJoinRequest, User loginUser);

    Boolean teamQuit(TeamQuitRequest teamQuitRequest, User loginUser);

    List<UserTeamVO> teamMyJoin(TeamMyJoinAndCreateRequest teamMyJoinAndCreateRequest, User loginUser);

    List<UserTeamVO> teamMyCreate(TeamMyJoinAndCreateRequest teamMyJoinAndCreateRequest, User loginUser);

}
