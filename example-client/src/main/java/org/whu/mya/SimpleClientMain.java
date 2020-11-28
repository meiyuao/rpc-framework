package org.whu.mya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.proxy.RpcClientProxy;
import org.whu.mya.remoting.transport.ClientTransport;
import org.whu.mya.remoting.transport.netty.client.NettyClientTransport;
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
        System.out.println(context.getBean("HelloService1"));
//        HelloService helloService1 = (HelloService) context.getBean("HelloService1");
//        helloService1.hello(new Hello("111", "`"));
}
}
