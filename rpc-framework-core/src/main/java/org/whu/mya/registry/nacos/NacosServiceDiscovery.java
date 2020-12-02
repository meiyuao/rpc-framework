package org.whu.mya.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.whu.mya.extension.ExtensionLoader;
import org.whu.mya.loadbalance.LoadBalance;
import org.whu.mya.registry.ServiceDiscovery;
import org.whu.mya.registry.nacos.util.NacosNamingServiceUtils;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NacosServiceDiscovery implements ServiceDiscovery {
    private final Map<String, List<String>> serviceUrlListMap = new ConcurrentHashMap<>();
    private NamingService namingService = NacosNamingServiceUtils.NAMING_SERVICE;
    private final LoadBalance loadBalance;

    public NacosServiceDiscovery() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class)
                .getExtension("random");
    }


    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        List<Instance> instancesList = null;
        List<String> serviceUrlList = new ArrayList<>();

        try {
            instancesList = namingService.getAllInstances(rpcServiceName.split(":")[0],
                    rpcServiceName.split(":")[1]);
            for (Instance instance : instancesList) {
                serviceUrlList.add(instance.getIp() + ":" + instance.getPort());
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }


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
