package org.whu.mya.provider;

import org.whu.mya.entity.RpcServiceProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerivceProviderImpl implements ServiceProvider {
    private final Map<String, Object> serviceMap;

    public SerivceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
    }



    @Override
    public void addService(Object service, RpcServiceProperties rpcServiceProperties) {
        System.out.println("服务：" +service.getClass().getInterfaces()[0].getCanonicalName() + " 注册完成");
        serviceMap.put(service.getClass().getInterfaces()[0].getCanonicalName(), service);
    }

    @Override
    public void register(Object service, RpcServiceProperties rpcServiceProperties) {
        addService(service, rpcServiceProperties);
    }


    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        Object service = serviceMap.get(rpcServiceProperties.getServiceName());
        if (null == service) throw new RuntimeException();
        return service;
    }
}
