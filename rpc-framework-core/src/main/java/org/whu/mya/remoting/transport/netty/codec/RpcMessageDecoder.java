package org.whu.mya.remoting.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.whu.mya.compress.Compress;
import org.whu.mya.compress.gzip.GzipCompress;
import org.whu.mya.enums.SerializationTypeEnum;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.remoting.dto.RpcRequest;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.serialize.Serializer;
import org.whu.mya.serialize.kryo.KryoSerializer;
import org.whu.mya.serialize.protostuff.ProtostuffSerializer;

import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    /**
     *
     * @param maxFrameLength
     * @param lengthFieldOffset
     * @param lengthFieldLength
     * @param lengthAdjustment
     * @param initialBytesToStrip
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
            return decodeFrame(in);
        }
        return null;
    }



    private Object decodeFrame(ByteBuf in) {
        // read the first 4 bit, anc compare
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i])
                throw new IllegalArgumentException("Unkown magic code:" + tmp.toString());
        }
        // read the version
        byte version = in.readByte();
        if (version != RpcConstants.VERSION)
            throw new RuntimeException("version is not compatible:" + version);
        // read the length
        int fullLength = in.readInt();
        System.out.println("fullLength:" + fullLength);
        // read messagetType
        byte messagetType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();

        RpcMessage rpcMessage = RpcMessage.builder()
                .codec(codecType)
                .compress(compressType)
                .requestId(requestId)
                .messageType(messagetType).build();
        if (messagetType == RpcConstants.HEARTBEAT_REQUEST_TYPE)
            rpcMessage.setData(RpcConstants.PING);
        else if (messagetType == RpcConstants.HEARTBEAT_RESPONSE_TYPE)
            rpcMessage.setData(RpcConstants.PONG);
        else {
            int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
            if (bodyLength > 0) {
                byte[] bs = new byte[bodyLength];
                in.readBytes(bs);
                Compress compress = new GzipCompress();
                bs = compress.decompress(bs);
                Serializer serializer = null;
                if (codecType == SerializationTypeEnum.KRYO.getCode())
                    serializer = new KryoSerializer();
                else if (codecType == SerializationTypeEnum.PROTOSTUFF.getCode())
                    serializer = new ProtostuffSerializer();
                if (messagetType == RpcConstants.REQUEST_TYPE) {
                    rpcMessage.setData(serializer.deserialize(bs, RpcRequest.class));
                }
                if (messagetType == RpcConstants.RESPONSE_TYPE) {
                    rpcMessage.setData(serializer.deserialize(bs, RpcResponce.class));
                }

            }
        }
        return rpcMessage;
    }
}
