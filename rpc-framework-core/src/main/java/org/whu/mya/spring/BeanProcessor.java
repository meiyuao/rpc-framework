package org.whu.mya.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.proxy.RpcClientProxy;
import org.whu.mya.spring.config.ReferenceBean;

import java.lang.reflect.Field;

@Component
public class BeanProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ReferenceBean) {
            System.out.println("ggggg");
        }
            return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName);
        if (bean instanceof ReferenceBean) {
            System.out.println(bean.getClass());
            ReferenceBean referenceBean = (ReferenceBean) bean;
            try {
                RpcClientProxy rpcClientProxy = new RpcClientProxy(RpcServiceProperties.builder().group(referenceBean.getGroup()).build());

                bean = rpcClientProxy.getProxy(
                        ClassLoader.getSystemClassLoader().loadClass(referenceBean.getInterfaceName())
                );
//                System.out.println(bean.getClass());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
