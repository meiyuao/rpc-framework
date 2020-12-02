package org.whu.mya;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.stereotype.Component;
import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;
import org.whu.mya.util.MyApplicationContextUtil;

public class SimpleClientMain {

//    @NacosInjected
//    private  NamingService namingService;
//
//    public void registerService(String name) {
//        Instance instance = new Instance();
//        instance.setIp("127.0.0.1");
//        try {
//            System.out.println(namingService);
//            namingService.registerInstance("HelloService", instance);
//        } catch (NacosException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws Throwable {

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
////
        MyApplicationContextUtil.setApplicationContext(context);
        HelloService helloService1 = (HelloService) context.getBean("HelloService1");
        helloService1.hello(new Hello("111", "`"));
        Thread.sleep(60000);
        helloService1.hello(new Hello("111", "`"));

//        Thread.sleep(1000);
//        SimpleClientMain simpleClientMain = new SimpleClientMain();
//        simpleClientMain.registerService("helloService");

    }
}
