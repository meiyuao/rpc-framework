package org.whu.mya.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.registry.ServiceRegistry;
import org.whu.mya.remoting.transport.netty.server.NettyServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImpl implements ServiceProvider {
    private final Map<String, Object> serviceMap;
    private final ServiceRegistry serviceRegistry;


    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }




    @Override
    public void addService(Object service, RpcServiceProperties rpcServiceProperties) {
        String serviceName = rpcServiceProperties.toRpcServiceName();
        serviceMap.put(serviceName, service);
    }

    @Override
    public void register(Object service, RpcServiceProperties rpcServiceProperties) {
        addService(service, rpcServiceProperties);
    }


    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        Object service = serviceMap.get(rpcServiceProperties.toRpcServiceName());
        if (null == service) throw new RuntimeException();
        return service;
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            String serviceName = service.getClass().getInterfaces()[0].getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            this.addService(service, rpcServiceProperties);
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(), new InetSocketAddress(host, NettyServer.PORT));
            System.out.println("服务：" +service.getClass().getCanonicalName() + " 注册完成");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}