package com.aop.aspect;

import com.aop.annotation.*;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: cxx
 * @Date: 2019/10/3 14:24
 */
public class AspectHandler implements IAspectHandler {
    // Aspect切面类
    private static Map<String/*匹配拦截路径*/, Pair<Object, CheckResult/*运行哪些通知*/>> aspectPointMap = new HashMap<>();

    public AspectHandler(Map<String, Object> iocMap){
        init(iocMap);
    }

    @Override
    public Pair<Object, CheckResult> getAspectInstance(String pointCanonicalName) {
        for (Map.Entry<String, Pair<Object, CheckResult>> entry : aspectPointMap.entrySet()) {
            if (match(entry.getKey(), pointCanonicalName)){
                return entry.getValue();
            }
        }
        return null;
    }

    private boolean match(String regex, String pointCanonicalName){
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(pointCanonicalName);
        while (matcher.find()){
            return true;
        }
        return false;
    }

    private static void init(Map<String, Object> iocMap){
        List<Object> aspects = filterAspect(iocMap);
        parseAspect(aspects);
    }

    /**
     * 过滤Aspcet
     * @param iocMap
     * @return
     */
    private static List<Object> filterAspect(Map<String, Object> iocMap){
        List<Object> list = new ArrayList<Object>();
        if (iocMap.isEmpty()){
            return list;
        }
        Iterator<Map.Entry<String, Object>> iterator = iocMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getValue().getClass().isAnnotationPresent(MyAspect.class)){
                list.add(entry.getValue());
                iterator.remove();
            }
        }
        return list;
    }

    /**
     * 解析Aspcet
     * @param aspectBeans
     */
    private static void parseAspect(List<Object> aspectBeans){
        aspectBeans.forEach(e -> {
            CheckResult checkResult = new CheckResult();
            String pointCutValue = "";
            Class<?> clazz = e.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(MyPointcut.class)){
                    MyPointcut myPointcut = method.getAnnotation(MyPointcut.class);
                    if (myPointcut == null){
                        throw new RuntimeException("未定义pointcut");
                    }
                    pointCutValue = myPointcut.value();
                    continue;
                }
                if (method.isAnnotationPresent(MyBefore.class)){
                    MyBefore myBefore = method.getAnnotation(MyBefore.class);
                    if (myBefore == null){
                        continue;
                    }
                    checkResult.setRunBefore(isRun(myBefore.value()));
                }
                if (method.isAnnotationPresent(MyAround.class)){
                    MyAround myAround = method.getAnnotation(MyAround.class);
                    if (myAround == null){
                        continue;
                    }
                    checkResult.setRunAround(isRun(myAround.value()));
                }
                if (method.isAnnotationPresent(MyAfter.class)){
                    MyAfter myAfter = method.getAnnotation(MyAfter.class);
                    if (myAfter == null){
                        continue;
                    }
                    checkResult.setRunAfter(isRun(myAfter.value()));
                }
                if (method.isAnnotationPresent(MyAfterReturning.class)){
                    MyAfterReturning myAfterReturning = method.getAnnotation(MyAfterReturning.class);
                    if (myAfterReturning == null){
                        continue;
                    }
                    checkResult.setRunAfterReturning(isRun(myAfterReturning.value()));
                }
                if (method.isAnnotationPresent(MyAfterThrowing.class)){
                    MyAfterThrowing myAfterThrowing = method.getAnnotation(MyAfterThrowing.class);
                    if (myAfterThrowing == null){
                        continue;
                    }
                    checkResult.setRunAfterThrowing(isRun(myAfterThrowing.value()));
                }
            }
            aspectPointMap.put(pointCutValue, new Pair<>(e, checkResult));
        });
    }

    /**
     * 是否运行该通知
     * @param value
     * @return
     */
    private static boolean isRun(String value){
        if (value.isEmpty()){
            return false;
        }
        value = value.trim();
        if (!value.contains("()")){
            throw new RuntimeException("切面注解有误");
        }
        return value.contains("myPointcut");
    }

    public static void main(String[] args) {
        String value = "com.demo.UserService";
        Pattern p = Pattern.compile("com.demo.*");
        Matcher matcher = p.matcher(value);
        while (matcher.find()){
            String v = matcher.group(0);
            System.out.println(v);
        }
    }
}
