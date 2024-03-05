package com.dzy.job.once;

import com.dzy.mapper.UserMapper;
import com.dzy.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author Dzy
 * @Date 2024/2/22  14:48
 */

@Component
public class InsertUserInfoData {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    /**
     @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
     public void doInsertUser() {
     StopWatch stopWatch = new StopWatch();
     stopWatch.start();
     final int INSERT_NUM = 1000;
     final int INSERT_GROUP = 10;
     List<User> userList = new ArrayList<>();
     for (int i = 0; i < INSERT_NUM; i++) {
     User user = new User();
     user.setUserName("yangge");
     user.setUserAccount("xxxx");
     user.setUserPassword("123456");
     user.setGender("0");
     user.setBirthday(LocalDate.now());
     user.setPhone("1236722");
     user.setEmail("xx@qq.com");
     user.setAvatarUrl("1111111");
     user.setUserStatus(0);
     user.setTags("[]");
     userList.add(user);
     }
     userService.saveBatch(userList, 1000);
     stopWatch.stop();
     System.out.println(stopWatch.getTotalTimeMillis());
     }
     */
}
