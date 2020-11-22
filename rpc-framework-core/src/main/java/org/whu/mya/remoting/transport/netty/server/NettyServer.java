package org.whu.mya.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
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

public class NettyServer {
    public static final int PORT = 9998;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);

    public void registerService() {
        ApplicationContext context = MyApplicationContextUtil.getContext();
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            try {
                Object obj =  context.getBean(name);
                if (obj instanceof ServiceBean) {
                    ServiceBean serviceBean = (ServiceBean) obj;
                    serviceProvider.publishService(
                            ClassLoader.getSystemClassLoader().loadClass(serviceBean.getRef()).getDeclaredConstructor().newInstance()
                            , RpcServiceProperties.builder().group(serviceBean.getGroup()).build());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    public void registerService(Object service, RpcServiceProperties rpcServiceProperties) {
//        serviceProvider.publishService(service, rpcServiceProperties);
//    }


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
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(serviceHandlerGroup, new NettyServerHandler());
                        }
                    });

            // 绑定端口，同步等待绑定成功
            ChannelFuture future = serverBootstrap.bind(host, PORT).sync();
            if (future.isSuccess()) System.out.println("服务端已启动"+future.channel().localAddress());
            System.out.println("开始注册服务:");
            registerService();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }

    }
}
