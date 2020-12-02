package org.whu.mya.registry.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.spring.beans.factory.annotation.NamingMaintainServiceBeanBuilder;
import com.alibaba.nacos.spring.beans.factory.annotation.NamingServiceBeanBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.whu.mya.registry.ServiceRegistry;
import org.whu.mya.registry.nacos.util.NacosNamingServiceUtils;
import org.whu.mya.spring.config.RegistryConfig;
import org.whu.mya.util.MyApplicationContextUtil;

import java.net.InetSocketAddress;

public class NacosServiceRegistry implements ServiceRegistry {

    private NamingService namingService = NacosNamingServiceUtils.NAMING_SERVICE;



    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {

        try {
            Instance instance = new Instance();
            instance.setIp(inetSocketAddress.getHostString());
            instance.setPort(inetSocketAddress.getPort());
            this.namingService.registerInstance(rpcServiceName.split(":")[0],
                    rpcServiceName.split(":")[1],
                    instance);


//
//            System.out.println(namingService.getAllInstances(rpcServiceName.split(":")[0],
//                    rpcServiceName.split(":")[1]  ).get(0).getIp());

        } catch (NacosException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void unregisterService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        try {
            Instance instance = new Instance();
            instance.setIp(inetSocketAddress.getHostString());
            instance.setPort(inetSocketAddress.getPort());
            this.namingService.deregisterInstance(rpcServiceName.split(":")[0],
                    rpcServiceName.split(":")[1],
                    instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
