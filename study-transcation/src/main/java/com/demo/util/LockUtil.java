package com.demo.util;

import com.demo.redis.RedisService;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/10/23 11:49
 * Desc:
 */
public class LockUtil {
    @Resource
    private RedisService redisService;

    public long lock(LockEnum lockOp, String key, boolean needCheck) {
        long result = lock(lockOp, key);
        if (needCheck && result == 0L) {
            throw new RuntimeException(lockOp.getErrMsg());
        }
        return result;
    }

    public long lock(LockEnum lockOp, String key, boolean wait, long waitTime, int pollingCount) {
        long result;
        int index = 0;
        while ((result = lock(lockOp, key)) == 0L && wait && pollingCount > index) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                //ignore
            } finally {
                index++;
            }
        }
        return result;
    }

    public long unlock(LockEnum lockOp, String key) {
        String lockKey = getLockKey(lockOp, key);
        return redisService.del(lockKey);
    }

    private long lock(LockEnum lockOp, String key) {
        String lockKey = getLockKey(lockOp, key);
        return redisService.setnx(lockKey, lockOp.getExpiredTime(), "1");
    }

    private String getLockKey(LockEnum lockOp, String key) {
        return lockOp.getKey() + key;
    }

}
