package com.dzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzy.model.domain.UserTeam;
import com.dzy.service.UserTeamService;
import com.dzy.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
 * @author DZY
 * @description 针对表【user_team(用户队伍关系表)】的数据库操作Service实现
 * @createDate 2024-02-28 12:47:29
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




