package org.whu.mya.remoting.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.whu.mya.enums.CompressTypeEnum;
import org.whu.mya.enums.SerializationTypeEnum;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.spring.config.SerializeConfig;
import org.whu.mya.util.MyApplicationContextUtil;

import java.net.InetSocketAddress;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到返回的结果啦" + msg);
        if (msg instanceof RpcMessage) {
            RpcMessage tmp = (RpcMessage) msg;
            byte messageType = tmp.getMessageType();
            if (messageType == RpcConstants.RESPONSE_TYPE) {
                RpcResponce<Object> response = (RpcResponce<Object>) tmp.getData();
                unprocessedRequests.complete(response);
            } else if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                System.out.println("heart[{"+ tmp.getData() +"}]");
            }
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                System.out.println("客户端写空闲");
                Channel channel = channelProvider.get((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();

                // set codec
                SerializeConfig serializeConfig = (SerializeConfig) MyApplicationContextUtil.getBean("serialize");
                rpcMessage.setCodec(SerializationTypeEnum.getCode(serializeConfig.getType()));

                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                rpcMessage.setData(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage);
            }
            if (state == IdleState.READER_IDLE) {
                System.out.println("客户端读空闲");

            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端先关闭了与我客户端的连接，所以客户端也关闭连接");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
