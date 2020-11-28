package org.whu.mya.registry;

import org.springframework.context.ApplicationContext;
import org.whu.mya.extension.SPI;

import java.net.InetSocketAddress;

@SPI
public interface ServiceRegistry {

    /**
     * 注册服务
     * @param rpcServiceName
     * @param inetSocketAddress
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

    /**
     * 下线服务
     * @param rpcServiceName
     * @param inetSocketAddress
     */
    void unregisterService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
