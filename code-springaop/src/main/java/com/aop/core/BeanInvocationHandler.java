package com.aop.core;

import com.aop.annotation.AdviceEnum;
import com.aop.aspect.AbstractAspect;
import com.aop.aspect.CheckResult;
import com.aop.aspect.JoinPoint;
import javafx.util.Pair;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/3 15:35
 * @Desc: 环绕通知先不实现
 */
public class BeanInvocationHandler implements InvocationHandler {
    // 目标类
    private Object target;
    // 切面相关信息
    private Pair<Object, CheckResult> acpectResult;

    public BeanInvocationHandler(Object target, Pair<Object, CheckResult> acpectResult) {
        this.target = target;
        this.acpectResult = acpectResult;
    }

    /**
     * 内部类实现环绕通知
     */
    class MyJoinPoint implements JoinPoint{
        private Method method;
        private Object[] args;

        public MyJoinPoint(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }

        @Override
        public Object proceed() {
            try {
                return method.invoke(target, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 切面类
        AbstractAspect aspcet = (AbstractAspect) acpectResult.getKey();
        // 通知方式
        CheckResult runType = acpectResult.getValue();

        boolean isAround = runType.isRunAround();
        JoinPoint joinPoint = null;
        if (isAround){
            joinPoint = new MyJoinPoint(method, args);
        }
        Object result = null;
        try {
            // 1.前置通知
            if (runType.isRunBefore()){
                runAspectInstance(AdviceEnum.BEFORE, aspcet, args);
            }
            // 2.环绕通知
            if (isAround){
                result = aspcet.around(joinPoint);
            }else {
                result = method.invoke(target, args);
            }
            // 3.返回通知
            if (runType.isRunAfterReturning()){
                runAspectInstance(AdviceEnum.AFTER_RETURNING, aspcet, args, result);
            }
            return result;
        }catch (Exception e){
            // 4.异常通知
            if (runType.isRunAfterThrowing()){
                runAspectInstance(AdviceEnum.AFTER_THROWING, aspcet, args, e);
            }
        }finally {
            // 5.后置通知
            if (runType.isRunAfter()){
                runAspectInstance(AdviceEnum.AFTER, aspcet, args);
            }
        }
        return result;
    }


    private void runAspectInstance(AdviceEnum adviceEnum, AbstractAspect aspect, Object[] args){
        this.runAspectInstance(adviceEnum, aspect, args, null, null);
    }

    private void runAspectInstance(AdviceEnum adviceEnum, AbstractAspect aspect, Object[] args, Object result){
        this.runAspectInstance(adviceEnum, aspect, args, null, result);
    }

    private void runAspectInstance(AdviceEnum adviceEnum, AbstractAspect aspect, Object[] args, Throwable e){
        this.runAspectInstance(adviceEnum, aspect, args, e, null);
    }

    /**
     * 执行切面实例
     * @param adviceEnum
     * @param aspect
     * @param args
     */
    private void runAspectInstance(AdviceEnum adviceEnum, AbstractAspect aspect, Object[] args, Throwable e, Object result){
        try {
            switch (adviceEnum){
                case BEFORE:{
                    aspect.before();
                    break;
                }
                case AFTER_RETURNING:{
                    aspect.afterReturning(result);
                    break;
                }
                case AFTER_THROWING:{
                    aspect.afterThrowable(e);
                    break;
                }
                case AFTER:{
                    aspect.after();
                    break;
                }
                default:{
                    break;
                }
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }
}
