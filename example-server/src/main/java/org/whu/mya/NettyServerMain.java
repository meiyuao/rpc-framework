package org.whu.mya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.whu.mya.remoting.transport.netty.server.NettyServer;
import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;

@ComponentScan("org.whu.mya")
public class NettyServerMain {

    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"application-context.xml"}
        );

        NettyServer nettyServer = new NettyServer();
        nettyServer.start();
    }
}
