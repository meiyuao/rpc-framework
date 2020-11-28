package org.whu.mya.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.whu.mya.registry.ServiceRegistry;
import org.whu.mya.registry.zk.util.CuratorUtils;


import java.net.InetSocketAddress;

public class ZkServiceRegistry implements ServiceRegistry {


    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        //
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
//        try {
//            CuratorUtils.getZkClient().getData().usingWatcher(new Watcher() {
//                @Override
//                public void process(WatchedEvent watchedEvent) {
//                    System.out.println("事件类型"+ watchedEvent.getType());
//                }
//            }).forPath(servicePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void unregisterService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        try {
            CuratorUtils.getZkClient().delete().forPath(servicePath);
            System.out.println("服务： " + rpcServiceName + "  下线");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
