package com;

import com.ethan.controller.TestController;
import com.ethan.ioc.SpringIoc;

public class Main {
    public static void main(String[] args) {
        SpringIoc ioc = new SpringIoc();
        TestController controller = (TestController) ioc.getInstance(TestController.class.getName());
        controller.Test();
    }
}
