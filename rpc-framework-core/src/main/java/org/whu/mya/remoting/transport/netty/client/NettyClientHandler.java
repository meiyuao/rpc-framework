package org.whu.mya.remoting.transport.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.remoting.dto.RpcResponce;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
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
                RpcResponce<Object> responce = (RpcResponce<Object>) tmp.getData();
                unprocessedRequests.complete(responce);
            }
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端关闭了");
        ctx.close();
//        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
