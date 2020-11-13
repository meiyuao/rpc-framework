package org.whu.mya.entity;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceProperties {

    private String serviceName;

    public String toRpcServiceName() {
        return this.getServiceName();
    }
}
