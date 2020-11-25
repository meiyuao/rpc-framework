package org.whu.mya.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {

    KRYO((byte) 0x01, "kryo"),
    PROTOSTUFF((byte) 0x02, "protostuff"),
    DEFAULT ((byte) 0x03, "kryo");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationTypeEnum c : SerializationTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static Byte getCode(String name) {
        for (SerializationTypeEnum c : SerializationTypeEnum.values()) {
            if (name.equals(c.getName())) {
                return c.code;
            }
        }
        return null;
    }

}
