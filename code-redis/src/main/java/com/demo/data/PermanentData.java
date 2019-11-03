package com.demo.data;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: cxx
 * @Date: 2019/11/3 20:35
 * 持久化数据
 */
public class PermanentData {
    private PermanentData(){

    }

    private static PermanentData data = new PermanentData();

    public static PermanentData getInstance(){
        return data;
    }

    //资源所保存的路径
    private static File listProfile = new File("F:" + File.separator + "File" + File.separator + "list.bin");
    private static File mapProfile = new File("F:" + File.separator + "File" + File.separator + "map.bin");

    /*
     * 向文件中写入资源，使用ObjectOutputStream能够在原有文件的基础上追加文件
     */
    public  void writetoListProfile() {
        //检查文件是否存在
        if (!listProfile.getParentFile().exists()) {
            listProfile.getParentFile().mkdirs();
        }
        //创建文件
        if (!listProfile.exists()) {
            try {
                listProfile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(listProfile));
            os.writeObject(DbData.list);
            System.out.println("向文件中写入数据" + DbData.list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearListProfile(){
        if (listProfile.exists() && listProfile.length()!=0) {
            try  {
                listProfile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * 从文件中加载资源，使用ObjectIntputStream来接收
    * */
    public  void readFromListProfile() {
        if (listProfile.exists() && listProfile.length()!=0) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(listProfile))) {
                DbData.list = (Map<String, List<String>>) is.readObject();
                System.out.println(DbData.list + "从文件中读数据");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  void writetoMapProfile() {
        //检查文件是否存在
        if (!mapProfile.getParentFile().exists()) {
            mapProfile.getParentFile().mkdirs();
        }
        //创建文件
        if (!mapProfile.exists()) {
            try {
                mapProfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(mapProfile))) {
            os.writeObject(DbData.hash);
            System.out.println("向文件中写入数据" + DbData.hash);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    /*
     * 从文件中加载资源，使用ObjectIntputStream来接收
     * */
    public  void readFromMapProfile() {
        //要保证文件存在并且文件中有内容
        if (mapProfile.exists() && mapProfile.length()!=0) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(mapProfile))) {
                DbData.hash = (HashMap<String, HashMap<String,String>>) is.readObject();
                System.out.println(DbData.hash + "从文件中读数据");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
