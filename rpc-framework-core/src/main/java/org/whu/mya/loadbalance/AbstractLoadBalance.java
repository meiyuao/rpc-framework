package org.whu.mya.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance{

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
        if (serviceAddresses == null || serviceAddresses.size() == 0)
            return null;
        if (serviceAddresses.size() == 1) return serviceAddresses.get(0);

        return doSelect(serviceAddresses, rpcServiceName);
    }

    protected abstract String doSelect(List<String> serviceAddresses, String rpcServiceName);
}
