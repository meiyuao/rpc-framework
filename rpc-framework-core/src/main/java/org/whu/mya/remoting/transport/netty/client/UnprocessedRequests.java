package org.whu.mya.remoting.transport.netty.client;

import org.whu.mya.remoting.dto.RpcResponce;

import java.net.ResponseCache;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnprocessedRequests {
    private static final Map<String, CompletableFuture<RpcResponce<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();


    public void put(String requestId, CompletableFuture<RpcResponce<Object>> future) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);

    }

    public void complete(RpcResponce<Object> responce) {
        CompletableFuture<RpcResponce<Object>> future = UNPROCESSED_RESPONSE_FUTURES.get(responce.getRequestId());
        if (future != null) {
            future.complete(responce);
        }else {
            throw new IllegalStateException();
        }
    }
}
