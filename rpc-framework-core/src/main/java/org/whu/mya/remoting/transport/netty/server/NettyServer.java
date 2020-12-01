package org.whu.mya.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.FutureListener;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.ServiceProviderImpl;
import org.whu.mya.provider.ServiceProvider;
import org.whu.mya.remoting.transport.netty.codec.RpcMessageDecoder;
import org.whu.mya.remoting.transport.netty.codec.RpcMessageEncoder;
import org.whu.mya.spring.config.ServiceBean;
import org.whu.mya.util.MyApplicationContextUtil;

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class NettyServer {
    public static final int PORT = 9997;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    private Channel serverChannel;
    private ApplicationContext context;

    public NettyServer() {}
    public NettyServer(ApplicationContext ctx) {
        this.context = ctx;
    }

    public void closeServer() {
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel = null;
        }
    }


    @SneakyThrows
    public void start() {

        // 动态获取当前主机ip
        String host = InetAddress.getLocalHost().getHostAddress();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);

        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                Runtime.getRuntime().availableProcessors() * 2);


        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)  //option主要是针对boss线程组，child主要是针对worker线程组
                    .channel(NioServerSocketChannel.class)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。
                    // TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。true代表关闭Nagle算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(10, 10 ,10));
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(serviceHandlerGroup, new NettyServerHandler());
                        }
                    });

            // 绑定端口，同步等待绑定成功
            ChannelFuture future = serverBootstrap.bind(host, PORT).sync();
            if (future.isSuccess()) System.out.println("服务端已启动"+future.channel().localAddress());
            System.out.println("开始注册服务:");
            serviceProvider.registerService(context);
            // 等待服务端监听端口关闭

            serverChannel = future.channel();

            future.channel().closeFuture()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            serviceProvider.unregisterService();
                            bossGroup.shutdownGracefully();
                            workerGroup.shutdownGracefully();
                            serviceHandlerGroup.shutdownGracefully();
                        }
                    })
//            ;
                    .sync();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            System.out.println("netty server下线啦啊");
//
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            serviceHandlerGroup.shutdownGracefully();
        }

    }
}
