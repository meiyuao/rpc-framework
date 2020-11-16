package org.whu.mya.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.registry.zk.util.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;

public class ZkServiceDiscovery implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);

        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RuntimeException();
        }

        // 负载均衡选择服务器
        String targetServiceUrl = serviceUrlList.get(0);

        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
