package org.whu.mya.loadbalance;

import org.whu.mya.extension.SPI;

import java.util.List;

@SPI
public interface LoadBalance {

    String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName);
}
