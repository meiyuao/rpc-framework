package org.whu.mya.provider;

import org.springframework.context.ApplicationContext;
import org.whu.mya.entity.RpcServiceProperties;

import java.net.InetSocketAddress;

public interface ServiceProvider {

    /**
     * 注册spring环境配置的所有服务
     * @param ctx
     */
    void registerService(ApplicationContext ctx);

    void unregisterService();

    void addService(Object service, RpcServiceProperties rpcServiceProperties);

    Object getService(RpcServiceProperties rpcServiceProperties);

    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    void unPublishService(String service, InetSocketAddress inetSocketAddress);
}
