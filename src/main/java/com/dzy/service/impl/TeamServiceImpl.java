package com.dzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.constant.StatusCode;
import com.dzy.constant.TeamStatus;
import com.dzy.exception.BusinessException;
import com.dzy.mapper.TeamMapper;
import com.dzy.model.domain.Team;
import com.dzy.model.domain.User;
import com.dzy.model.domain.UserTeam;
import com.dzy.model.dto.team.*;
import com.dzy.service.TeamService;
import com.dzy.service.UserService;
import com.dzy.service.UserTeamService;
import com.dzy.model.vo.UserTeamVO;
import com.dzy.model.vo.UserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author DZY
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-02-26 20:54:58
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 添加队伍
     *
     * @param teamAddRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 队伍id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long teamAdd(TeamAddRequest teamAddRequest, User loginUser) {
        //请求参数是否为空
        if (teamAddRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        //是否登录
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long userId = loginUser.getUserId();
        //todo 提出来封装成发一个方法
        Team team = new Team();
        try {
            BeanUtils.copyProperties(teamAddRequest, team);
        } catch (Exception e) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        //校验team信息是否合法
        //队伍最大人数不超过20人
        Integer teamMaxNum = team.getTeamMaxNum();
        if (teamMaxNum < 1 || teamMaxNum > 20) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "超过队伍最大人数");
        }
        //队伍标题字符长度不超过20
        String teamName = team.getTeamName();
        if (StringUtils.isBlank(teamName) || teamName.length() > 20) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍名称不合法");
        }
        //队伍简介字符长度不超过512
        String teamDescription = team.getTeamDescription();
        if (StringUtils.isBlank(teamDescription) || teamDescription.length() > 512) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍简介不合法");
        }
        //队伍状态是否公开（int）,默认为 0（公开）
        Integer teamStatus = Optional.ofNullable(team.getTeamStatus()).orElse(0);
        TeamStatus teamStatusEnum = TeamStatus.getTeamStatusEnumByKey(teamStatus);
        if (teamStatusEnum == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍状态不合法");
        }
        //如果是加密状态,一定要有密码,且密码<=32
        String teamPassword = team.getTeamPassword();
        if (TeamStatus.ENCRYPT.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(teamPassword) || teamPassword.length() > 32) {
                throw new BusinessException(StatusCode.PARAM_ERROR, "队伍密码不合法");
            }
        }
        //超时时间 > 当前时间
        Date teamExpireTime = team.getTeamExpireTime();
        if (teamExpireTime != null && new Date().after(teamExpireTime)) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍过期时间不合法");
        }
        //用户最多创建5个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamUserId", userId);
        long hasCreateTeamCount = this.count(queryWrapper);
        if (hasCreateTeamCount >= 5) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "超过队伍创建数量");
        }
        //事务
        //插入队伍信息到队伍表
        //team.setTeamId(null);
        //队伍创建者的id
        team.setTeamUserId(userId);
        int insert = teamMapper.insert(team);
        Long teamId = team.getTeamId();
        if (insert < 1 || teamId == null) {
            throw new BusinessException(StatusCode.DATABASE_ERROR, "队伍创建失败");
        }
        //插入用户-队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        boolean save = userTeamService.save(userTeam);
        if (!save) {
            throw new BusinessException(StatusCode.DATABASE_ERROR, "队伍创建失败");
        }
        return teamId;
    }

    /**
     * 删除队伍
     *
     * @param teamDeleteRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 是否成功删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean teamDelete(TeamDeleteRequest teamDeleteRequest, User loginUser) {
        if (teamDeleteRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Long teamId = teamDeleteRequest.getTeamId();
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍不存在");
        }
        if (!team.getTeamUserId().equals(loginUser.getUserId())) {
            throw new BusinessException(StatusCode.ADMIN_ERROR, "无权限");
        }
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        boolean remove = userTeamService.remove(queryWrapper);
        if (!remove) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return this.removeById(teamId);
    }

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest 队伍请求参数封装对象
     * @param loginUser
     * @return 是否成功更新
     */
    @Override
    public boolean teamUpdate(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(teamUpdateRequest, team);
        } catch (Exception e) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        //查询队伍是否存在
        Long teamId = team.getTeamId();
        if (teamId == null || teamId < 0) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        Team searchTeam = this.getById(teamId);
        if (searchTeam == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍不存在");
        }
        //只有管理员或者队伍的创建者可以修改
        boolean isAdmin = userService.isAdmin(loginUser);
        Long teamUserId = team.getTeamUserId();
        Long teamUpdateRequestTeamUserId = teamUpdateRequest.getTeamUserId();
        if (!teamUpdateRequestTeamUserId.equals(teamUserId) && !isAdmin) {
            throw new BusinessException(StatusCode.ADMIN_ERROR);
        }
        //todo 如果用户传入的新值和老值一致，就不用 update 了（可自行实现，降低数据库使用次数）
        //如果队伍状态改为加密，必须要有密码
        Integer teamStatus = team.getTeamStatus();
        String teamPassword = team.getTeamPassword();
        TeamStatus teamStatusEnum = TeamStatus.getTeamStatusEnumByKey(teamStatus);
        if (TeamStatus.ENCRYPT.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(teamPassword)) {
                throw new BusinessException(StatusCode.PARAM_ERROR, "加密状态下没有密码");
            }
        }
        //更新成功
        int update = teamMapper.updateById(team);
        if (update < 1) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return true;
    }

    /**
     * 查询所有队伍
     *
     * @return 队伍列表
     */
    @Override
    public List<Team> teamSearchAll() {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        List<Team> teamList = teamMapper.selectList(queryWrapper);
        return teamList;
    }

    /**
     * 指定条件查询队伍
     *
     * @return 队伍列表
     */
    @Override
    public List<UserTeamVO> teamSearchBySpecificField(TeamQueryRequest teamQueryRequest, User loginUser) {
        if (teamQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        if (loginUser == null) {
            throw new BusinessException(StatusCode.NO_LOGIN_ERROR);
        }
        boolean isAdmin = userService.isAdmin(loginUser);
        //从请求参数中取出队伍名称等查询条件，如果存在则作为查询条件
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        Long teamId = teamQueryRequest.getTeamId();
        if (teamId != null && teamId > 0) {
            queryWrapper.eq("teamId", teamId);
        }
        String teamName = teamQueryRequest.getTeamName();
        if (StringUtils.isNotBlank(teamName)) {
            queryWrapper.like("teamName", teamName);
        }
        String teamDescription = teamQueryRequest.getTeamDescription();
        if (StringUtils.isNotBlank(teamDescription)) {
            queryWrapper.like("teamDescription", teamDescription);
        }
        //可以通过某个关键词同时对名称和描述查询
        String teamSearchText = teamQueryRequest.getTeamSearchText();
        if (StringUtils.isNotBlank(teamSearchText)) {
            queryWrapper.and(qw -> qw.like("name", teamSearchText).or().like("description", teamSearchText));
        }
        Integer teamMaxNum = teamQueryRequest.getTeamMaxNum();
        if (teamMaxNum != null && teamMaxNum > 0) {
            queryWrapper.eq("teamMaxNum", teamMaxNum);
        }
        //只有管理员才能查看加密还有非公开的房间
        Integer teamStatus = teamQueryRequest.getTeamStatus();
        TeamStatus teamStatusEnum = TeamStatus.getTeamStatusEnumByKey(teamStatus);
        if (teamStatusEnum == null) {
            teamStatusEnum = TeamStatus.PUBLIC;
        }
        if (!isAdmin && TeamStatus.ENCRYPT.equals(teamStatusEnum)) {
            throw new BusinessException(StatusCode.ADMIN_ERROR);
        }
        if (teamStatus != null && teamStatus > -1) {
            queryWrapper.eq("teamStatus", teamStatusEnum.getStatus());
        }
        //不展示已过期的队伍（根据过期时间筛选）
        queryWrapper.and(qw->qw.isNull("teamExpireTime").or().ge("teamExpireTime", new Date()));
        Long teamUserId = teamQueryRequest.getTeamUserId();
        //根据创建人查询
        if (teamUserId != null && teamUserId > 0) {
            queryWrapper.eq("teamUserId", teamUserId);
        }
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<UserTeamVO> userTeamVOList = new ArrayList<>();
        //关联查询已加入队伍的用户信息
        for (Team team : teamList) {
            Long teamCreateUserId = team.getTeamUserId();
            if (teamCreateUserId == null || teamCreateUserId < 0) {
                continue;
            }
            User user = userService.getById(teamCreateUserId);
            UserTeamVO userTeamVO = new UserTeamVO();
            BeanUtils.copyProperties(team, userTeamVO);
            //用户信息
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                userTeamVO.setCreateUser(userVO);
            }
            userTeamVOList.add(userTeamVO);
        }
        return userTeamVOList;
    }

    /**
     * 分页查询队伍
     *
     * @param teamQueryRequest 队伍请求参数封装对象
     * @return 分页队伍
     */
    @Override
    public Page<Team> teamSearchByPage(TeamQueryRequest teamQueryRequest) {
        if (teamQueryRequest == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR);
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(teamQueryRequest, team);
        } catch (Exception e) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> page = new Page<>(teamQueryRequest.getPageCurrent(), teamQueryRequest.getPageSize());
        Page<Team> teamIPage = (Page<Team>) teamMapper.selectPage(page, queryWrapper);
        return teamIPage;
    }

    /**
     * 根据队伍id查询队伍
     *
     * @param teamId 队伍id
     * @return 队伍
     */
    @Override
    public Team teamSearchById(long teamId) {
        return teamMapper.selectById(teamId);
    }

    /**
     * 用户加入队伍
     * 分布式锁
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    @Override
    public Boolean teamJoin(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        if (loginUser == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        //队伍必须存在
        Long teamId = teamJoinRequest.getTeamId();
        if (teamId == null || teamId < 0) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(StatusCode.PARAM_NULL_ERROR, "队伍不存在");
        }
        //只能加入未过期的队伍
        Date teamExpireTime = team.getTeamExpireTime();
        if (teamExpireTime != null && teamExpireTime.before(new Date())) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "队伍已过期");
        }
        //禁止加入私有的队伍
        Integer teamStatus = team.getTeamStatus();
        TeamStatus teamStatusEnum = TeamStatus.getTeamStatusEnumByKey(teamStatus);
        if (TeamStatus.PRIVATE.equals(teamStatusEnum)) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "禁止加入私有的队伍");
        }
        String teamPassword = team.getTeamPassword();
        String teamJoinRequestTeamPassword = teamJoinRequest.getTeamPassword();
        //如果加入的队伍是加密的，必须密码匹配才可以
        if (TeamStatus.ENCRYPT.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(teamPassword) || !teamPassword.equals(teamJoinRequestTeamPassword)) {
                throw new BusinessException(StatusCode.PARAM_ERROR, "加入队伍密码错误");
            }
        }
        //加锁
        RLock lock = redissonClient.getLock("pma:teamjoin:lock");
        try {
            while (true){
                if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                    //用户最多加入 5 个队伍
                    Long userId = loginUser.getUserId();
                    if (userId == null || userId < 0) {
                        throw new BusinessException(StatusCode.PARAM_ERROR);
                    }
                    QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("userId", userId);
                    long hasUserJoinTeamCount = userTeamService.count(queryWrapper);
                    if (hasUserJoinTeamCount >= 5) {
                        throw new BusinessException(StatusCode.PARAM_ERROR, "用户加入队伍超过上限");
                    }

                    //只能加入未满队伍
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("teamId", teamId);
                    long hasTeamIncludeUserCount = userTeamService.count(queryWrapper);
                    if (hasTeamIncludeUserCount >= team.getTeamMaxNum()) {
                        throw new BusinessException(StatusCode.PARAM_ERROR, "队伍已满");
                    }
                    //不能加入自己的队伍，不能重复加入已加入的队伍（幂等性）
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("teamId", teamId);
                    queryWrapper.eq("userId", userId);
                    long hasJoinTeamUserCount = userTeamService.count(queryWrapper);
                    if (hasJoinTeamUserCount > 0) {
                        throw new BusinessException(StatusCode.PARAM_ERROR, "用户已加入队伍");
                    }
                    //新增队伍 - 用户关联信息
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }
        } catch (InterruptedException e) {
            log.error("redis set teamjoin error", e);
            return false;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 用户退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean teamQuit(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        if (loginUser == null) {
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = this.getById(teamId);
        Long userId = loginUser.getUserId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(queryUserTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(StatusCode.PARAM_ERROR, "未加入该队伍");
        } else if (count == 1) {
            UserTeam queryTeamCount = new UserTeam();
            queryTeamCount.setTeamId(teamId);
            QueryWrapper<UserTeam> teamCountQueryWrapper = new QueryWrapper<>(queryTeamCount);
            long teamCount = userTeamService.count(teamCountQueryWrapper);
            //队伍还有用户
            if (teamCount > 1) {
                return userTeamService.remove(queryWrapper);
            } else if (teamCount == 1) {
                //是否是创建者
                if (team.getTeamUserId().equals(userId)) {
                    userTeamService.remove(queryWrapper);
                    //删除队伍
                    return this.removeById(teamId);
                }
            }
        }
        return false;
    }

    @Override
    public List<UserTeamVO> teamMyJoin(TeamMyJoinAndCreateRequest teamMyJoinAndCreateRequest, User loginUser) {
        Long teamUserId = teamMyJoinAndCreateRequest.getTeamUserId();
        Long userId = loginUser.getUserId();
        //登录者id是否和前端请求参数用户id一致
        //只能查看自己的
        if(!userId.equals(teamUserId)){
            throw new BusinessException(StatusCode.ADMIN_ERROR);
        }
        //用户创建的队伍列表
        List<UserTeamVO> userCreateTeamList = getUserCreateTeamList(userId);
        //用户创建和加入的队伍列表
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        List<UserTeam> userJoinAndCreateTeamList = userTeamService.list(queryWrapper);
        List<UserTeamVO> userJoinTeamList = new ArrayList<>();
        //用户创建的队伍列表是否为空,是，则userJoinAndCreateTeamList全部都是加入的队伍
        if(CollectionUtils.isEmpty(userCreateTeamList)){
            for (UserTeam userTeam : userJoinAndCreateTeamList) {
                Long teamId = userTeam.getTeamId();
                Team team = this.getById(teamId);
                UserTeamVO userTeamVO = teamToUserTeamVO(team);
                userJoinTeamList.add(userTeamVO);
            }
        }else{
            List<Long> userCreateTeamIdList = new ArrayList<>();
            for (UserTeamVO userTeamVO : userCreateTeamList) {
                userCreateTeamIdList.add(userTeamVO.getTeamId());
            }
            for (UserTeam userTeam : userJoinAndCreateTeamList) {
                if(userCreateTeamIdList.contains(userTeam.getTeamId())){
                    break;
                }
                Long teamId = userTeam.getTeamId();
                Team team = this.getById(teamId);
                UserTeamVO userTeamVO = teamToUserTeamVO(team);
                userJoinTeamList.add(userTeamVO);
            }
        }
        return userJoinTeamList;
    }

    @Override
    public List<UserTeamVO> teamMyCreate(TeamMyJoinAndCreateRequest teamMyJoinAndCreateRequest, User loginUser) {
        Long teamUserId = teamMyJoinAndCreateRequest.getTeamUserId();
        Long userId = loginUser.getUserId();
        //登录者id是否和前端请求参数用户id一致
        //只能查看自己的
        if(!userId.equals(teamUserId)){
            throw new BusinessException(StatusCode.ADMIN_ERROR);
        }
        return getUserCreateTeamList(userId);
    }

    /**
     * 查询用户创建的队伍
     *
     * @param userId
     * @return
     */
    public List<UserTeamVO> getUserCreateTeamList(Long userId){
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamUserId",userId);
        List<Team> userJoinAndCreateTeamList = this.list(queryWrapper);
        List<UserTeamVO> userTeamVOList = new ArrayList<>();
        for (Team team : userJoinAndCreateTeamList) {
            UserTeamVO userTeamVO = teamToUserTeamVO(team);
            userTeamVOList.add(userTeamVO);
        }
        return userTeamVOList;
    }

    /**
     * 队伍信息脱敏，转VO
     *
     * @param team
     * @return
     */
    public UserTeamVO teamToUserTeamVO(Team team){
        UserTeamVO userTeamVO = new UserTeamVO();
        BeanUtils.copyProperties(team,userTeamVO);
        return userTeamVO;
    }

    /**
     * 获取队伍当前人数
     *
     * @param teamId
     * @return
     */
    public long getTeamUserNum(Long teamId){
        if(teamId == null || teamId<0){
            throw new BusinessException(StatusCode.PARAM_ERROR);
        }
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId",teamId);
        long count = userTeamService.count(queryWrapper);
        if(count<0) {
            throw new BusinessException(StatusCode.DATABASE_ERROR);
        }
        return count;
    }
}




