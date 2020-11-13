package org.whu.mya.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.whu.mya.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements Serializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
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
