package org.whu.mya.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取单例对象的工厂类
 */
public final class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new HashMap<>();

    public static void put(Object object) {
        if (!OBJECT_MAP.containsKey(object.getClass().toString()))
            OBJECT_MAP.put(object.getClass().toString(), object);
    }
    public static <T> T getInstance(Class<T> clazz) {
        String key = clazz.toString();
        Object instance = OBJECT_MAP.get(key);
        if (instance == null) { // 双重加锁的单例模式
            synchronized (SingletonFactory.class) {
                instance =OBJECT_MAP.get(key);
                if (instance == null) {
                    try {
                        instance = clazz.getDeclaredConstructor().newInstance();
                        OBJECT_MAP.put(key, instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return clazz.cast(instance);
    }
}
