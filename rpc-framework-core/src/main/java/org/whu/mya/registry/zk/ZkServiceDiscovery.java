package org.whu.mya.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.FactoryBean;
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
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZkServiceDiscovery implements ServiceDiscovery {
    public static final List<String> interstedServiceNameList = new ArrayList<>();
    private final Map<String, List<String>> serviceUrlListMap = new ConcurrentHashMap<>();

    private final LoadBalance loadBalance;
    public ZkServiceDiscovery() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class)
                .getExtension("random");
        CuratorUtils.doSubscribe();
    }

    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {

        List<String> serviceUrlList = CuratorUtils.getUrls(rpcServiceName);

        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RuntimeException();
        }

        // 负载均衡选择服务器
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcServiceName);
        System.out.println("负载均衡选择的服务器为：" + targetServiceUrl);

        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

}
