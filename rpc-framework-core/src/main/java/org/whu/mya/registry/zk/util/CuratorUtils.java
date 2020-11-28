package org.whu.mya.registry.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.whu.mya.spring.config.RegistryConfig;
import org.whu.mya.util.MyApplicationContextUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static CuratorFramework zkClient;
    private static String defaultZookeeperAddress = "192.168.200.176:2181";

    private CuratorUtils(){

    }


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

    public static void createPersistentNode(CuratorFramework zkClient, String servicePath) {
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

    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> res = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            System.out.println(servicePath);
            res = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.putIfAbsent(rpcServiceName, res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
