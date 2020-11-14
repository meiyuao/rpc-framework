package org.whu.mya.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.whu.mya.remoting.dto.RpcRequest;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements Serializer {

    // Kryo is not thread safe. Each thread should have its own Kryo, Input, and Output instances.
    private final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
//            kryo.register(RpcRequest.class);
//            kryo.register(RpcResponce.class);
//            kryo.setRegistrationRequired(true); // 建议设置为true，必须提前注册序列化的对象类型

            return kryo;
        }
    };

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);

        Kryo kryo = kryoThreadLocal.get();
        kryo.writeObject(output, obj);

        // 如果我们使用完ThreadLocal对象而没有手动删掉，
        // 那么后面的请求就有机会使用到被使用过的ThreadLocal对象
        kryoThreadLocal.remove();
        return output.toBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        Kryo kryo = kryoThreadLocal.get();
        Object obj = kryo.readObject(input, clazz);
        kryoThreadLocal.remove();
        return clazz.cast(obj);
    }
}
