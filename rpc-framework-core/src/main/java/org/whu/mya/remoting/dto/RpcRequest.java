package org.whu.mya.remoting.dto;

import lombok.*;
import org.whu.mya.entity.RpcServiceProperties;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest  {

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paraTypes;
    private String version;
    private String group;


    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder().serviceName(this.getInterfaceName())
                .build();
    }
}
