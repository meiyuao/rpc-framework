package org.whu.mya.provider;

import org.whu.mya.entity.RpcServiceProperties;

public interface ServiceProvider {

    void addService(Object service, RpcServiceProperties rpcServiceProperties);

    void register(Object service, RpcServiceProperties rpcServiceProperties);

    Object getService(RpcServiceProperties rpcServiceProperties);

}
