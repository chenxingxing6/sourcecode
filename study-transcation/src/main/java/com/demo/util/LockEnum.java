package com.demo.util;

/**
 * User: lanxinghua
 * Date: 2019/10/23 11:50
 * Desc:
 */
public enum LockEnum {
    EXE_DO("testkey:", 60, "添加业态商品中，请稍后!")
    ;

    /**
     * 操作key
     */
    private String key;
    /**
     * 操作过期时间
     */
    private int expiredTime;
    /**
     * 异常消息
     */
    private String errMsg;

    LockEnum(String key, int expiredTime, String errMsg) {
        this.key = key;
        this.expiredTime = expiredTime;
        this.errMsg = errMsg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
