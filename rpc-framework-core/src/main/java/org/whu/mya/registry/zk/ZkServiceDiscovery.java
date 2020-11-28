package org.whu.mya.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.ApplicationContext;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.loadbalance.LoadBalance;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.registry.zk.util.CuratorUtils;
import org.whu.mya.spring.config.ReferenceBean;
import org.whu.mya.spring.config.ServiceBean;
import org.whu.mya.util.MyApplicationContextUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class ZkServiceDiscovery implements ServiceDiscovery {

    private final LoadBalance loadBalance;
    private ApplicationContext context;
    public ZkServiceDiscovery() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class)
                .getExtension("random");
//        doSubscribe();
    }

    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);

        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw null;
        }

        // 负载均衡选择服务器
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcServiceName);
        System.out.println("负载均衡选择的服务器为：" + targetServiceUrl);

        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

    @Override
    public void doSubscribe() {
        String[] beanNames = context.getBeanDefinitionNames();
        for (String name : beanNames) {
            try {
                Object obj =  context.getBean(name);
                System.out.println(obj);
                if (obj instanceof ReferenceBean) {
                    System.out.println("fdfdfdaasddxx");
//                    ServiceBean serviceBean = (ServiceBean) obj;
//                    Object service = ClassLoader.getSystemClassLoader().loadClass(serviceBean.getRef()).getDeclaredConstructor().newInstance();
//                    RpcServiceProperties serviceProperties = RpcServiceProperties.builder().group(serviceBean.getGroup()).build();
//
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
