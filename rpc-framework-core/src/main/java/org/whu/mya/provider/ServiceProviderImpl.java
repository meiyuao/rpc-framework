package org.whu.mya.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.registry.ServiceRegistry;
import org.whu.mya.remoting.transport.netty.server.NettyServer;
import org.whu.mya.spring.config.RegistryConfig;
import org.whu.mya.spring.config.ServiceBean;
import org.whu.mya.util.MyApplicationContextUtil;

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
        serviceRegistry = ExtensionLoader
                .getExtensionLoader(ServiceRegistry.class)
                .getExtension(((RegistryConfig) MyApplicationContextUtil.getBean("registry")).getType());
    }

    /**
     * 注册spring配置的所有service
     * @param context
     */
    @Override
    public void registerService(ApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            try {
                Object obj =  context.getBean(name);
                if (obj instanceof ServiceBean) {
                    ServiceBean serviceBean = (ServiceBean) obj;
                    Object service = ClassLoader.getSystemClassLoader().loadClass(serviceBean.getRef()).getDeclaredConstructor().newInstance();
                    RpcServiceProperties serviceProperties = RpcServiceProperties.builder().group(serviceBean.getGroup()).build();
                    this.publishService(service, serviceProperties);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void unregisterService() {

        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, NettyServer.PORT);
        for (String serviceName: serviceMap.keySet()) {
            this.unPublishService(serviceName, inetSocketAddress);
        }
    }


    /**
     * 用于 add service到 Map
     * @param service
     * @param rpcServiceProperties
     */
    @Override
    public void addService(Object service, RpcServiceProperties rpcServiceProperties) {
        String serviceName = rpcServiceProperties.toRpcServiceName();
        serviceMap.put(serviceName, service);
    }



    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        Object service = serviceMap.get(rpcServiceProperties.toRpcServiceName());
        if (null == service) throw new RuntimeException();
        return service;
    }

    /**
     * 用于发布service到服务注册中心
     * @param service
     * @param rpcServiceProperties
     */
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


    @Override
    public void unPublishService(String serviceName, InetSocketAddress inetSocketAddress) {
        serviceRegistry.unregisterService(serviceName, inetSocketAddress);
    }


}
