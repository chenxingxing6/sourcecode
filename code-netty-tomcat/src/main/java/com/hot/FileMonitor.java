package com.hot;

import sun.nio.ch.IOUtil;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/10/9 15:00
 * Desc: 开启热部署,com.catalina.servlet下的文件
 * 修改servlet文件，保存，会自动实现热更新部署
 */
public class FileMonitor {
    private static final String projectName = "code-netty-tomcat";
    private static final String packagePath = "com/catalina/servlet/";

    public static void main(String[] args) {
       new FileMonitor().start();
    }

    public void start(){
        System.out.println("开启热部署.....");
        try {
            Path path = Paths.get(projectName + "/src/main/java/" + packagePath);
            WatchService watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            new Thread(() -> {
                while (true) {
                    try {
                        WatchKey key = watcher.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.kind() == StandardWatchEventKinds.OVERFLOW){
                                // 事件可能是lost or discarded
                                continue;
                            }
                            Path p = (Path) event.context();
                            System.out.println("------------ start 热部署 --------------");
                            hotDeploy(p);
                            System.out.println("------------- end 热部署 --------------");
                        }
                        if (!key.reset()){
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            TimeUnit.SECONDS.sleep(60*10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 热部署
     * @param path
     */
    public static void hotDeploy(Path path){
        String fileName = path.toFile().getName();
        // java源码路径
        String prefixPath = projectName + "/src/main/java/" + packagePath;
        String sourceCodePath = prefixPath+fileName;
        fileName = fileName.replace(".java", ".class");
        try {
            System.gc();
            String p = projectName + "/target/classes/"+ packagePath +fileName;
            File oldFile = new File(p);
            oldFile.delete();
            // 对源码进行编译
            doCompile(sourceCodePath);
            // 编译后端class文件移动到target对应的目录中去
            moveFile(prefixPath + fileName, p);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void doCompile(String sourceCodePath){
        try {
            System.out.println("源码文件进行编译："+sourceCodePath);
            File file = new File(sourceCodePath);
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable iterable = manager.getJavaFileObjects(file);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void moveFile(String srcPathStr, String desPathStr) {
        try{
            // 创建输入输出流对象
            File file = new File(desPathStr);
            if (!file.exists()){
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);
            //创建搬运工具
            byte datas[] = new byte[1024*8];
            //创建长度
            int len = 0;
            //循环读取数据
            while((len = fis.read(datas))!=-1){
                fos.write(datas,0,len);
            }
            //释放资源
            fis.close();
            fis.close();
            File srcFile = new File(srcPathStr);
            srcFile.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
