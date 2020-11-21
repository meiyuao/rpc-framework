package org.whu.mya;

import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.proxy.RpcClientProxy;
import org.whu.mya.remoting.transport.ClientTransport;
import org.whu.mya.remoting.transport.netty.client.NettyClientTransport;
import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;

public class SimpleClientMain {
    public static void main(String[] args) throws Throwable {
        ClientTransport clientTransport = new NettyClientTransport();

        RpcClientProxy rpcClientProxy = new RpcClientProxy(RpcServiceProperties.builder().group("group2").build());
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);


        for (int i = 0; i < 10; i++) {
//            Thread.sleep(1000);
            String hello =  helloService.hello(new Hello("111", "" + i));
            System.out.println(hello);
        }



    }
}
