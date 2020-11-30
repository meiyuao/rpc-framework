package org.whu.mya.registry.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.registry.zk.ZkServiceDiscovery;
import org.whu.mya.registry.zk.ZkServiceRegistry;
import org.whu.mya.spring.config.RegistryConfig;
import org.whu.mya.util.MyApplicationContextUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public final class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static CuratorFramework zkClient = getZkClient();
    private static String defaultZookeeperAddress = "192.168.200.176:2181";
    private static boolean isFirstAddWatch = true;
//    private static final ZkServiceDiscovery serviceDiscovery = (ZkServiceDiscovery) ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");


    private CuratorUtils(){ }

    public static CuratorFramework getZkClient() {

        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }

        RegistryConfig registry = (RegistryConfig) MyApplicationContextUtil.getBean("registry");
        if (registry != null) {
            defaultZookeeperAddress = registry.getAddress() + ":" + registry.getPort();
        }

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(defaultZookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }

    public static void doSubscribe() {
        for (String serviceName : MyApplicationContextUtil.getInterestedService()) {
            updateChildrenNodes(serviceName);
            addWatcher(serviceName);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isFirstAddWatch = false;
            }
        }).start();
    }

    public static List<String> getUrls(String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        return null;
    }


    public static void createEphemeralNode(CuratorFramework zkClient, String servicePath) {
        try {
            if (REGISTERED_PATH_SET.contains(servicePath) || zkClient.checkExists().forPath(servicePath) != null) {
                System.out.println("已经注册了");
            }else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(servicePath);
            }
            REGISTERED_PATH_SET.add(servicePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新获取最新的服务提供者ip
     * @param rpcServiceName
     */
    public static void updateChildrenNodes(String rpcServiceName) {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            List<String> res = zkClient.getChildren().forPath(servicePath);
            System.out.println("list size: " + res.size());
            SERVICE_ADDRESS_MAP.putIfAbsent(rpcServiceName, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听节点 rpcServiceName 下子节点ip的变化
     * @param rpcServiceName
     */
    public static void addWatcher (String rpcServiceName) {
        String path = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
//        System.out.println("在" + path+" 添加watcher");

        TreeCache treeCache = new TreeCache(zkClient, path);

        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
//                System.out.println("监听到节点数据变化，类型："+event.getType()+",路径："+event.getData().getPath());

                if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED) {
                    System.out.println("有服务下线, 重新获取服务提供者..");
                    updateChildrenNodes(rpcServiceName);
                } else if (event.getType() == TreeCacheEvent.Type.NODE_ADDED){
                    if (isFirstAddWatch) return;
                    if (event.getData().getStat().getNumChildren() == 0) {
                        System.out.println("有服务上线, 重新获取服务提供者..");
                        updateChildrenNodes(rpcServiceName);
                    }
                }
            }
        });

        try {
            treeCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            CuratorUtils.getZkClient().getData().usingWatcher(new CuratorWatcher() {
//                @Override
//                public void process(WatchedEvent watchedEvent) {
//                    System.out.println(watchedEvent.getPath());
//                    System.out.println(watchedEvent.getState());
//                    System.out.println("事件类型"+ watchedEvent.getType());
//                }
//            }).forPath(servicePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

}
