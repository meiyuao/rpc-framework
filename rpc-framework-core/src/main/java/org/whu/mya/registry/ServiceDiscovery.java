package org.whu.mya.registry;


import org.whu.mya.extension.SPI;

import java.net.InetSocketAddress;

@SPI
public interface ServiceDiscovery {

    /**
     * lookup service by rpcServiceName
     * @param rpcServiceName
     * @return
     */
    InetSocketAddress lookupService(String rpcServiceName);

    void doSubscribe();
}
