package com.ioc.core;

import com.ioc.annotation.MyAutowired;
import com.ioc.annotation.MyComponent;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: cxx
 * @Date: 2019/10/3 11:47
 */
public class MyIoc {
    private static HashMap<String/*类全名*/, Object> beanFactory = new HashMap<String, Object>();
    public static String scanPackage = null;
    private static List<String> classNames = null;
    private static Properties prop = null;

    private MyIoc(){
    }

    static {
        try {
            prop = new Properties();
            String path = MyIoc.class.getClassLoader().getResource("ioc.properties").getPath();
            prop.load(new FileInputStream(new File(path)));
        }catch (Exception e){
            e.printStackTrace();
        }
        scanPackage = prop.getProperty("scan.package");
        if (scanPackage == null){
            throw new RuntimeException("请设置扫描路径");
        }
        classNames = new ArrayList<String>();
        init();
    }

    public static HashMap<String, Object> getBeanFactory() {
        return beanFactory;
    }

    public static Object getObject(String completeClassName){
        return beanFactory.get(completeClassName);
    }

    public static void updateBean(String completeClassName, Object object){
        beanFactory.put(completeClassName, object);
    }

    public static void init(){
        try {
            // 1.scan包
            doScan(scanPackage);
            // 2.实例化对象放入IOC容器
            doInstance();
            // 3.依赖注入
            doAutowired();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void doScan(String scanPackage){
        String basePath = MyIoc.class.getClassLoader().getResource("").getPath();
        basePath = basePath + scanPackage.replaceAll("\\.", "/");
        File file = new File(basePath);
        if (file.exists()){
            for (File f : file.listFiles()) {
                if (f.isDirectory()){
                    doScan(scanPackage + "." + f.getName());
                }else {
                    classNames.add(scanPackage + "." + f.getName().replace(".class", ""));
                }
            }
        }
    }

    private static void doInstance(){
        try {
            if (classNames.isEmpty()){
                return;
            }
            for (String className : classNames) {
                Class clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(MyComponent.class)){
                    beanFactory.put(className, clazz.newInstance());
                    if (clazz.getInterfaces().length >0){
                        beanFactory.put(clazz.getInterfaces()[0].getTypeName(), clazz.newInstance());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void doAutowired(){
        try {
            if (beanFactory.isEmpty()){
                return;
            }
            for (Map.Entry<String, Object> entry : beanFactory.entrySet()) {
                // 获取所有的属性
                Field[] fields = entry.getValue().getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MyAutowired.class)){
                        Object obj = beanFactory.get(field.getType().getTypeName());
                        field.setAccessible(true);
                        field.set(entry.getValue(), obj);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
