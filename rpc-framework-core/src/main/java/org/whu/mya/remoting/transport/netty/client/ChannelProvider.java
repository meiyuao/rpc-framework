package org.whu.mya.remoting.transport.netty.client;


import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelProvider {
    private final NettyClient nettyClient;
    private final Map<String, Channel> channelMap;


    public ChannelProvider() {
        nettyClient = new NettyClient();
        channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                // 该通道还处于连接状态
                System.out.println("已经有老通道还处于活跃状态");
                return channel;
            }else channelMap.remove(key);
        }
        Channel channel = nettyClient.doConnect(inetSocketAddress);
        channelMap.put(key, channel);
        return channel;
    }



}
