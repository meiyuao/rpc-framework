package org.whu.mya.loadbalance.loadbalancer;

import org.whu.mya.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    private final Map<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    protected String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        int identityHashCode = System.identityHashCode(serviceAddresses);

        ConsistentHashSelector selector = selectors.get(rpcServiceName);

//        if (selector == null || selector.)
        return null;
    }

    static class ConsistentHashSelector {

    }
}
