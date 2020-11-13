package org.whu.mya.serviceImpl;

import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(Hello hello) {
        System.out.println("HelloService: " + hello.getMessage());
        return hello.getDescription();
    }
}
