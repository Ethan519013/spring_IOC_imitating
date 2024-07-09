package com.ethan.ioc;

import com.ethan.annotation.Autowired;
import com.ethan.annotation.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;

public class SpringIoc {

    private String classPath;           // 类路径
    private String packageName;         // 包名
    private List<String> beanNames;
    private List<String> filePaths;

    private Map<String,Object> beans;

    public SpringIoc() {
        
        try {
            initPath();
            scan();
            initBeanNames();
            initBeans();
        } catch (FileNotFoundException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取实例
     */
    public Object getInstance(String beanName) {
        return beans.get(beanName);
    }

    /**
     * 为所有带有 Component 注解的类创建对象, 并注入到对应的属性中
     */
    private void initBeans() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        beans = new HashMap<>();
        // 找到所有带 '@Component' 注解的类, 为其创建对象
        for (String beanName : beanNames) {
            Class<?> clazz =  Class.forName(beanName);
            if (isComponent(clazz)) {
                Object obj = clazz.newInstance();
                beans.put(clazz.getName(), obj);
            }
        }
        // 为所有带 '@Autowired' 注解的属性注入对象
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (isAutowired(field)) {
                    String fieldName = field.getType().getName();
                    Object obj = beans.get(fieldName);
                    field.setAccessible(true);
                    field.set(entry.getValue(), obj);
                }
            }
        }
    }

    /**
     * 判断属性是否带有 Autowired 注解
     * @param field
     * @return
     */
    private boolean isAutowired(Field field) {
        return field.getAnnotation(Autowired.class) != null;
    }

    /**
     * 判断类是否带有 Component 注解
     * @param clazz
     * @return
     */
    private boolean isComponent(Class<?> clazz) {
        return clazz.getAnnotation(Component.class) != null;
    }

    /**
     * 得到所有类的全限定名
     */
    private void initBeanNames() {
        beanNames = new ArrayList<>();
        for (String filePath : filePaths) {
//            String beanName = packageName
//                    + filePath.substring(classPath.length())
//                    .split(".java")[0]
//                    .replaceAll(Matcher.quoteReplacement(File.separator), ".");
            String beanName = filePath.substring(classPath.length()-1).split(".class")[0]
                    .replaceAll(Matcher.quoteReplacement(File.separator), ".");
            beanNames.add(beanName);
        }
    }

    /**
     * 扫描所有的文件, 保存到 filePath
     */
    private void scan() throws FileNotFoundException {
        filePaths = new ArrayList<>();
        File file = new File(classPath);
        if (!file.exists()) {
            throw new FileNotFoundException("未找到 " + classPath);
        }
        // 遍历文件树
        File[] files;
        Queue<File> fileQueue = new LinkedList<>();
        fileQueue.offer(file);
        while (!fileQueue.isEmpty()) {
            file = fileQueue.poll();
            files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    fileQueue.offer(file1);
                } else {
                    filePaths.add(file1.getPath());
                }
            }
        }

    }

    /**
     * 初始化类路径, 这里直接写死
     */
    private void initPath() {
//        classPath = "D:\\Files\\IDEA-WorkSpace\\project\\spring_IOC_imitating\\src\\com\\ethan";
        // 从当前线程获取项目编译文件的根目录(out)
        classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        packageName = "com.ethan";
    }
}
