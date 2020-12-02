package org.whu.mya.registry.nacos.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.whu.mya.spring.config.RegistryConfig;
import org.whu.mya.util.MyApplicationContextUtil;

public class NacosNamingServiceUtils {
    public final static NamingService NAMING_SERVICE = getNamingService();

    private static NamingService getNamingService() {
        try {
            RegistryConfig registryConfig = (RegistryConfig) MyApplicationContextUtil.getBean("registry");
            return NacosFactory.createNamingService(registryConfig.getAddress() + ":" + registryConfig.getPort());
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }
}
