package org.whu.mya.remoting.transport.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.whu.mya.enums.CompressTypeEnum;
import org.whu.mya.enums.SerializationTypeEnum;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.remoting.dto.RpcRequest;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.remoting.transport.ClientTransport;

import java.net.InetSocketAddress;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.concurrent.CompletableFuture;

public class NettyClientTransport implements ClientTransport {
    private final ChannelProvider channelProvider;
    private final UnprocessedRequests unprocessedRequests;
    private final ServiceDiscovery serviceDiscovery;
    @Override
    protected void finalize() throws Throwable {

    }

    public NettyClientTransport() {
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    public CompletableFuture<RpcResponce<Object>> sendRpcRequest(RpcRequest request) {
        // build return value
        CompletableFuture<RpcResponce<Object>> resultFuture = new CompletableFuture<>();

        String rpcServiceName = request.toRpcProperties().toRpcServiceName();
        InetSocketAddress inetSocketAddress= serviceDiscovery.lookupService(rpcServiceName);

        Channel channel = channelProvider.get(inetSocketAddress);

        if (channel != null && channel.isActive()) {

//            System.out.println("客户端准备发送数据啦");
//            System.out.println("远端地址：" + channel.remoteAddress());
            unprocessedRequests.put(request.getRequestId(), resultFuture);

            // 构建RpcMessage
            RpcMessage message = new RpcMessage();
            message.setData(request);
            message.setCodec(SerializationTypeEnum.KRYO.getCode()); // 序列化方式
            message.setCompress(CompressTypeEnum.GZIP.getCode()); // 压缩方式
            message.setMessageType(RpcConstants.REQUEST_TYPE); // Message的类型
            ChannelFuture channelFuture = channel.writeAndFlush(message);
        }else{
            System.out.println("空");
        }

        return resultFuture;
    }
}
