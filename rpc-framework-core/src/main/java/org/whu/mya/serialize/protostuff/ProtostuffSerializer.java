package org.whu.mya.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.whu.mya.remoting.constants.RpcConstants;
import org.whu.mya.serialize.Serializer;

public class ProtostuffSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        Schema schema = RuntimeSchema.createFrom(obj.getClass());
        LinkedBuffer linkedBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] res = ProtostuffIOUtil.toByteArray(obj, schema, linkedBuffer);
        return res;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}
