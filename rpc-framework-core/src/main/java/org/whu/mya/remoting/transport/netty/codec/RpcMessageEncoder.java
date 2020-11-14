package org.whu.mya.remoting.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.whu.mya.compress.Compress;
import org.whu.mya.compress.gzip.GzipCompress;
import org.whu.mya.enums.SerializationTypeEnum;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.serialize.Serializer;
import org.whu.mya.serialize.kryo.KryoSerializer;
import org.whu.mya.serialize.protostuff.ProtostuffSerializer;

import java.util.concurrent.atomic.AtomicInteger;

public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) throws Exception {

        out.writeBytes(RpcConstants.MAGIC_NUMBER);
        out.writeByte(RpcConstants.VERSION);
        // leave a place to write the value of full length
        out.writerIndex(out.writerIndex() + 4);
        byte messageType = rpcMessage.getMessageType();
        out.writeByte(messageType);
        out.writeByte(rpcMessage.getCodec()); // 序列化类型
        out.writeByte(rpcMessage.getCompress()); // 压缩类型
        out.writeInt(ATOMIC_INTEGER.getAndIncrement());

        // build full length
        byte[] bodyBytes = null;
        int fullLength = RpcConstants.HEAD_LENGTH;
        // if message if not heartbeat message, fullLength = head length + body length
        if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
            && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            // serialize the body
            Serializer serializer = null;
            String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
            serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(codecName);
            bodyBytes = serializer.serialize(rpcMessage.getData());

            Compress gzipCompress = new GzipCompress();
            bodyBytes = gzipCompress.compress(bodyBytes);
            // compress the bytes

            fullLength += bodyBytes.length;
        }

        if (bodyBytes != null)
            out.writeBytes(bodyBytes);

        int writeIndex = out.writerIndex();
        out.writerIndex(writeIndex - fullLength + 5);
        out.writeInt(fullLength);
        out.writerIndex(writeIndex);

//        Serializer kryoSerializer = new KryoSerializer();
//        System.out.println(kryoSerializer.serialize(rpcMessage).length);
//        System.out.println(kryoSerializer.serialize(rpcMessage).toString());
//        out.writeBytes(kryoSerializer.serialize(rpcMessage));
    }
}
