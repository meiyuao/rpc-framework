package org.whu.mya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.whu.mya.remoting.transport.netty.server.NettyServer;
import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;
import org.whu.mya.spring.config.RegistryConfig;
import org.whu.mya.spring.config.ServiceBean;
import org.whu.mya.util.MyApplicationContextUtil;

public class NettyServerMain {

    public static void main(String[] args) throws Exception {


        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
        MyApplicationContextUtil.setApplicationContext(context);

        NettyServer nettyServer = new NettyServer(context);
        nettyServer.start();

        System.out.println("heihei");

        Thread.sleep(3000);
        nettyServer.closeServer();
    }
}
