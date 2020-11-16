package org.whu.mya;

import org.whu.mya.proxy.RpcClientProxy;
import org.whu.mya.remoting.transport.ClientTransport;
import org.whu.mya.remoting.transport.netty.client.NettyClientTransport;
import org.whu.mya.service.Hello;
import org.whu.mya.service.HelloService;

public class SimpleClientMain {
    public static void main(String[] args) throws Throwable {
        ClientTransport clientTransport = new NettyClientTransport();

        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);


        for (int i = 0; i < 1; i++) {
           String hello =  helloService.hello(new Hello("111", "" + i));
            System.out.println(hello);
        }


    }
}
