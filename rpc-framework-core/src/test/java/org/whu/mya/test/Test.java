package org.whu.mya.test;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.esotericsoftware.kryo.Kryo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.UnpooledDirectByteBuf;
import org.whu.mya.remoting.dto.RpcMessage;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.serialize.Serializer;
import org.whu.mya.serialize.kryo.KryoSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Test {
    @NacosInjected
    public static NamingService namingService;


    public static void main(String[] args) {
        System.out.println(namingService);
//        Serializer kryoSerializer = new KryoSerializer();
//
//        RpcMessage rpcMessage = new RpcMessage();
//        rpcMessage.setData("fdsfds");
//
//        byte[] bytes = kryoSerializer.serialize(rpcMessage);
//
//        System.out.println(kryoSerializer.deserialize(bytes, RpcMessage.class));
//        Map<String, RpcResponce<?>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();
//        UNPROCESSED_RESPONSE_FUTURES.put("1",null);

    }
}
