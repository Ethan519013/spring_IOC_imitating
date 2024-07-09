package com.ethan.service;

import com.ethan.annotation.Component;

@Component
public class UserService {
    public void add(String name, Integer age) {
        System.out.println(name);
        System.out.println(age);
    }
}
