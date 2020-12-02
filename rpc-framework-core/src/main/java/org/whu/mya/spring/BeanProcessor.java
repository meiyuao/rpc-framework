package org.whu.mya.spring;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.proxy.RpcClientProxy;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.spring.config.ReferenceBean;
import org.whu.mya.util.MyApplicationContextUtil;

import java.lang.reflect.Field;

@Component
public class BeanProcessor implements BeanPostProcessor {
//    @NacosInjected
//    private NamingService namingService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println(namingService);
        if (bean instanceof ReferenceBean) {
            ReferenceBean referenceBean = (ReferenceBean) bean;
            MyApplicationContextUtil.addInterestedService(
                    RpcServiceProperties.builder()
                            .serviceName(referenceBean.getClazz().getName())
                            .group(referenceBean.getGroup())
                            .build()
                            .toRpcServiceName()
            );
        }//        if (bean instanceof ReferenceBean) {
//            System.out.println(bean.getClass());
//            ReferenceBean referenceBean = (ReferenceBean) bean;
//            try {
//                RpcClientProxy rpcClientProxy = new RpcClientProxy(RpcServiceProperties.builder().group(referenceBean.getGroup()).build());
//
//                bean = rpcClientProxy.getProxy(
//                        ClassLoader.getSystemClassLoader().loadClass(referenceBean.getInterfaceName())
//                );
////                System.out.println(bean.getClass());
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }
}
