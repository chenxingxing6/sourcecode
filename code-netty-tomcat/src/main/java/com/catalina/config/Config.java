package com.catalina.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * User: lanxinghua
 * Date: 2019/10/9 10:50
 * Desc: 加载自定义配置
 */
public class Config {
    private static Map<String, String> config;

    public static void load(String... props){
        new Config(props);
    }

    public Config(String... props) {
        for (String path : props) {
            Properties p = new Properties();
            try {
                p.load(Config.class.getClassLoader().getResourceAsStream(path));
                config = new HashMap<String, String>();
                for (Object key : p.keySet()) {
                    String keyStr = key.toString();
                    config.put(keyStr, p.getProperty(keyStr));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取已加载的配置信息
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return config.get(key);
    }

    /**
     * 获取所有的key值
     * @return
     */
    public static Set<String> getKeys(){
        return config.keySet();
    }

    public static void main(String[] args) {
        Config.load("web.properties");
        System.out.println(Config.getKeys().toString());
    }
}
