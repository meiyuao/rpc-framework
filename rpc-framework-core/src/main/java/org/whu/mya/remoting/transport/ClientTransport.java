package org.whu.mya.remoting.transport;

import org.whu.mya.remoting.dto.RpcRequest;

/**
 * 发送RpcRequest
 */
public interface ClientTransport {

    /**
     * 发送RpcRequset去服务端并获取返回结果
     * @param request
     * @return
     */
    Object sendRpcRequest(RpcRequest request);
}
