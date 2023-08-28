package com.rzg.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedissonLimiterManagerTest {

    @Resource
    private RedissonLimiterManager redissonLimiterManager;

    @Test
    void doRateLimit() {
        String userId = "1";
        for (int i = 0; i < 3; i++) {
            redissonLimiterManager.doRateLimit(userId);
            System.out.println("成功");
        }
    }
}