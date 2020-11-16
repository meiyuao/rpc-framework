package org.whu.mya.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.SneakyThrows;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.SerivceProviderImpl;
import org.whu.mya.provider.ServiceProvider;
import org.whu.mya.remoting.transport.netty.codec.RpcMessageDecoder;
import org.whu.mya.remoting.transport.netty.codec.RpcMessageEncoder;

import java.net.InetAddress;

public class NettyServer {
    public static final int PORT = 9999;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(SerivceProviderImpl.class);

    public void registerService(Object service, RpcServiceProperties rpcServiceProperties) {
        serviceProvider.publishService(service, rpcServiceProperties);
    }


    @SneakyThrows
    public void start() {
        // 动态获取当前主机ip
        String host = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup)  //option主要是针对boss线程组，child主要是针对worker线程组
                    .channel(NioServerSocketChannel.class)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。
                    // TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。true代表关闭Nagle算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new RpcMessageDecoder());

                            pipeline.addLast(new NettyServerHandler());
                            pipeline.addLast(new RpcMessageEncoder());

                        }
                    });

            // 绑定端口，同步等待绑定成功
            ChannelFuture future = serverBootstrap.bind(host, PORT).sync();
            if (future.isSuccess()) System.out.println("服务端已启动"+future.channel().localAddress());
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
