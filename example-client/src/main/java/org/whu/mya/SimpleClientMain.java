package org.whu.mya;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;
import org.whu.mya.util.MyApplicationContextUtil;

public class SimpleClientMain {


    public static void main(String[] args) throws Throwable {
//        ClientTransport clientTransport = new NettyClientTransport();
//
//        RpcClientProxy rpcClientProxy = new RpcClientProxy(RpcServiceProperties.builder().group("group2").build());
//        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
//
//
//        for (int i = 0; i < 10; i++) {
////            Thread.sleep(1000);
//            String hello =  helloService.hello(new Hello("111", "" + i));
//            System.out.println(hello);
//            System.out.println(hello);
//        }
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
//
        MyApplicationContextUtil.setApplicationContext(context);
//        System.out.println(context.getBean("HelloService1"));
        HelloService helloService1 = (HelloService) context.getBean("HelloService1");
        helloService1.hello(new Hello("111", "`"));
        Thread.sleep(60000);
        helloService1.hello(new Hello("111", "`"));


    }
}
