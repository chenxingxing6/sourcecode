package com.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @Author: cxx
 * @Date: 2019/10/2 14:24
 */
public class MyClassLoader extends ClassLoader{
    private String baseDir;

    public MyClassLoader() {
        this.baseDir = this.getClass().getResource("").getFile();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String className = this.getClass().getPackage().getName() + "." + name;
        if (baseDir != null && baseDir != ""){
            File f = new File(baseDir + "/" + name +".class");
            ByteArrayOutputStream out = null;
            FileInputStream fis = null;
            if (f.exists()){
                try {
                    fis = new FileInputStream(f);
                    out = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = fis.read(b)) != -1){
                        out.write(b, 0, len);
                    }
                    return defineClass(className, out.toByteArray(), 0, out.size());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (fis != null){
                        try {
                            fis.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (out != null){
                        try {
                            out.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    f.delete();
                }
            }
        }
        return null;
    }
}
