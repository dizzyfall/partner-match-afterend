package com.dzy.job.scheduling;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.model.domain.User;
import com.dzy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Dzy
 * @Date 2024/2/25  22:35
 */
@Component
@Slf4j
public class PreUserCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private List<Long> mainUserList = Arrays.asList(1L, 2L);

    @Scheduled(cron = "0 0 0 * * *")
    public void doUserRecommendCache() {
        RLock lock = redissonClient.getLock("pma:preusercachejob:docache:lock");
        try {
            if (lock.tryLock(0, 30000L, TimeUnit.MILLISECONDS)) {
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = "pma:user:recommend:" + userId;
                    try {
                        redisTemplate.opsForValue().set(redisKey, userPage);
                    } catch (Exception e) {
                        log.error("redis set userScheduledKey error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("redis set userRecommendCache error", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
