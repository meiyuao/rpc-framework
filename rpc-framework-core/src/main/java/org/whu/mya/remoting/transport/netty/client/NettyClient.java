package org.whu.mya.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.remoting.transport.netty.codec.RpcMessageDecoder;
import org.whu.mya.remoting.transport.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;

public class NettyClient {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyClient() {

        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(5,5,5));
                        pipeline.addLast(new RpcMessageDecoder());
                        pipeline.addLast(new NettyClientHandler());
                        pipeline.addLast(new RpcMessageEncoder());
                    }
                });
    }

    /**
     * 连接某服务端
     * @param inetSocketAddress
     * @return
     */
    @SneakyThrows
    public Channel doConnect(final InetSocketAddress inetSocketAddress) {
        System.out.println("尝试连接"+inetSocketAddress.getAddress());
        final ChannelFuture future = bootstrap.connect(inetSocketAddress).sync();
        System.out.println("尝试成功");

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("The client has connected ["+ inetSocketAddress.toString() +"] successful!");
                }else {
                    throw new IllegalStateException();
                }
            }
        });

//        Thread.sleep(3000);
//        close();
        return future.channel();
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
