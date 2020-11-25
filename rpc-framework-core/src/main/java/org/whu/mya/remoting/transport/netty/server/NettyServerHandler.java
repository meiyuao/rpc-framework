package org.whu.mya.remoting.transport.netty.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.whu.mya.enums.SerializationTypeEnum;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.remoting.dto.RpcRequest;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.remoting.handler.RpcRequestHandler;
import org.whu.mya.serialize.Serializer;
import org.whu.mya.serialize.kryo.KryoSerializer;
import org.whu.mya.spring.config.SerializeConfig;
import org.whu.mya.util.MyApplicationContextUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequestHandler rpcRequestHandler;

    public NettyServerHandler() {
        rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有人来连接啦");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端收到数据啦");
        System.out.println("当前线程："+Thread.currentThread().getId());
        System.out.println((RpcMessage)msg);
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            byte messageType = rpcMessage.getMessageType();

            if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {

            }else {
                RpcRequest request = (RpcRequest) rpcMessage.getData();
                Object result = rpcRequestHandler.handle(request);
                rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);

                // set codec
                SerializeConfig serializeConfig = (SerializeConfig) MyApplicationContextUtil.getBean("serialize");
                if (serializeConfig != null)
                    rpcMessage.setCodec(SerializationTypeEnum.getCode(serializeConfig.getType()));
                else
                    rpcMessage.setCodec(SerializationTypeEnum.DEFAULT.getCode());

                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    RpcResponce<Object> rpcResponse = RpcResponce.success(result, request.getRequestId());
                    rpcMessage.setData(rpcResponse);
                }
            }
            ctx.channel().writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
    }

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
//        System.out.println(s);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();

    }
}
