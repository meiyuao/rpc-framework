package org.whu.mya;

import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.remoting.transport.netty.server.NettyServer;
import org.whu.mya.serviceImpl.HelloServiceImpl;

public class NettyServerMain {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.registerService(new HelloServiceImpl(), new RpcServiceProperties());
        nettyServer.start();
    }
}
