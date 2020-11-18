package org.whu.mya.serviceImpl;

import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;

public class HelloServiceImpl2 implements HelloService {
    @Override
    public String hello(Hello hello) {
        System.out.println("HelloService: " + hello.getMessage());
        return "HelloService2: " + hello.getDescription();
    }
}
