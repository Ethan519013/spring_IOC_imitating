package com.ethan;

import com.ethan.controller.TestController;
import com.ethan.ioc.SpringIoc;

public class Main {
    public static void main(String[] args) {
        SpringIoc ioc = new SpringIoc();
        // 通过 IOC 类获取需要的 Controller 对象
        TestController controller = (TestController) ioc.getInstance(TestController.class.getName());
        // 执行 Controller 对象中的方法
        controller.Test();

//        // 获取当前项目的根目录
//        System.out.println(System.getProperty("user.dir"));
//        // 获取Main类编译文件的目录
//        System.out.println(Main.class.getResource("").getPath());
//        // 通过当前的线程, 获取当前项目编译文件的目录
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }
}
