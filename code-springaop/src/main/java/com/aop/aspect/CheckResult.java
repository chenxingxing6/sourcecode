package com.aop.aspect;

/**
 * @Author: cxx
 * @Date: 2019/10/3 14:50
 */
public class CheckResult {
    private boolean isRunBefore = false;
    private boolean isRunAfter = false;
    private boolean isRunAfterReturning = false;
    private boolean isRunAfterThrowing = false;
    private boolean isRunAround = false;

    public boolean isRunBefore() {
        return isRunBefore;
    }

    public void setRunBefore(boolean runBefore) {
        isRunBefore = runBefore;
    }

    public boolean isRunAfter() {
        return isRunAfter;
    }

    public void setRunAfter(boolean runAfter) {
        isRunAfter = runAfter;
    }

    public boolean isRunAfterReturning() {
        return isRunAfterReturning;
    }

    public void setRunAfterReturning(boolean runAfterReturning) {
        isRunAfterReturning = runAfterReturning;
    }

    public boolean isRunAfterThrowing() {
        return isRunAfterThrowing;
    }

    public void setRunAfterThrowing(boolean runAfterThrowing) {
        isRunAfterThrowing = runAfterThrowing;
    }

    public boolean isRunAround() {
        return isRunAround;
    }

    public void setRunAround(boolean runAround) {
        isRunAround = runAround;
    }
}
