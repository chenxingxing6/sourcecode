package com.demo.util;

import com.demo.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/10/23 11:49
 * Desc:
 */
@Component
public class LockUtil {
    @Autowired
    private RedisService redisService;

    public boolean lock(LockEnum lockOp, String key, boolean needCheck) {
        boolean result = lock(lockOp, key);
        if (needCheck && !result) {
            throw new RuntimeException(lockOp.getErrMsg());
        }
        return result;
    }

    public boolean lock(LockEnum lockOp, String key, boolean wait, long waitTime, int pollingCount) {
        boolean result;
        int index = 0;
        while ((result = lock(lockOp, key) && wait && pollingCount > index)) {
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

    public boolean unlock(LockEnum lockOp, String key) {
        String lockKey = getLockKey(lockOp, key);
        return redisService.del(lockKey) > 0;
    }

    private boolean lock(LockEnum lockOp, String key) {
        String lockKey = getLockKey(lockOp, key);
        return redisService.setnx(lockKey, "value", lockOp.getExpiredTime());
    }

    private String getLockKey(LockEnum lockOp, String key) {
        return lockOp.getKey() + key;
    }
}
