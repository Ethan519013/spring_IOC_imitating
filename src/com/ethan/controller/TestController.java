package com.ethan.controller;

import com.ethan.annotation.Autowired;
import com.ethan.annotation.Component;
import com.ethan.service.UserService;

@Component
public class TestController {

    @Autowired
    private UserService userService;

    public void Test() {
        userService.add("张三", 18);
    }
}
