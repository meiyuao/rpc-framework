package org.whu.mya;

import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.remoting.transport.netty.server.NettyServer;
import org.whu.mya.serviceImpl.HelloServiceImpl;
import org.whu.mya.serviceImpl.HelloServiceImpl2;

public class NettyServerMain {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();

        nettyServer.registerService(new HelloServiceImpl(), RpcServiceProperties.builder().group("group1").build());
        nettyServer.registerService(new HelloServiceImpl2(), RpcServiceProperties.builder().group("group2").build());

        nettyServer.start();
    }
}
