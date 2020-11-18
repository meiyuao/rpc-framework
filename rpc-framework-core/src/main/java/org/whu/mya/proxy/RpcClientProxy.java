package org.whu.mya.proxy;

import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.remoting.dto.RpcRequest;
import org.whu.mya.remoting.dto.RpcResponce;
import org.whu.mya.remoting.transport.ClientTransport;
import org.whu.mya.remoting.transport.netty.client.NettyClientTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RpcClientProxy implements InvocationHandler {

    private final ClientTransport clientTransport;
    private final RpcServiceProperties serviceProperties;

    public RpcClientProxy() {
        clientTransport = new NettyClientTransport();
        serviceProperties = RpcServiceProperties.builder().group("").build();
    }

    public RpcClientProxy(RpcServiceProperties properties) {
        clientTransport = new NettyClientTransport();
        serviceProperties = RpcServiceProperties.builder().group(properties.getGroup()).build();
    }

    /**
     * 获取代理service类
     * @param clazz 接口类型
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz) {
        // return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
        // 因为我们在服务端传入的直接就是代表接口的class对象
        // clazz.getInterfaces()中的clazz应该是具体的一个接口实现类，而我们这里直接传入的是接口
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest rpcRequest = RpcRequest.builder()
                .parameters(args)
                .paraTypes(method.getParameterTypes())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .requestId(UUID.randomUUID().toString())
                .group(serviceProperties.getGroup())
                .build();

        CompletableFuture<RpcResponce<Object>> future = (CompletableFuture<RpcResponce<Object>>) clientTransport.sendRpcRequest(rpcRequest);

        RpcResponce<Object> response = future.get(); // 这里会阻塞

        return response.getData();
    }
}
