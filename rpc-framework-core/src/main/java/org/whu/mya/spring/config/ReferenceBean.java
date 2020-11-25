package org.whu.mya.spring.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.whu.mya.proxy.RpcClientProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Setter
@Getter
public class ReferenceBean<T> implements FactoryBean<T> {

    private Class<T> clazz;
    private String group;

    public ReferenceBean() {

    }

    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new RpcClientProxy());
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
