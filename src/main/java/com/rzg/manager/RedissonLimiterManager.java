package com.rzg.manager;

import com.rzg.common.ErrorCode;
import com.rzg.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 专门提供 RedissonLimiter 限流服务的
 */
@Service
public class RedissonLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     *
     * @param key 区分不同的限流器，比如不同的用户 id 应该区分统计
     */
    public void doRateLimit(String key){
        // 创建一个名称为 user_limiter 的限流器，每秒最多访问1次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 1, 1, RateIntervalUnit.SECONDS);

        // 每当一个操作来了之后，请求一个令牌
        boolean canOperate = rateLimiter.tryAcquire(1);
        if(!canOperate){
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
