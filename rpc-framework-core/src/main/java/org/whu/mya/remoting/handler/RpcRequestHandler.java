package org.whu.mya.remoting.handler;

import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.ServiceProviderImpl;
import org.whu.mya.provider.ServiceProvider;
import org.whu.mya.remoting.dto.RpcRequest;

import java.lang.reflect.Method;

public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;
    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest request) {


        Object service = serviceProvider.getService(request.toRpcProperties());
        return invokeTargetMethod(request, service);
    }

    private Object invokeTargetMethod(RpcRequest request, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParaTypes());
            System.out.println(method.getName());
            result = method.invoke(service, request.getParameters());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return result;
    }
}
