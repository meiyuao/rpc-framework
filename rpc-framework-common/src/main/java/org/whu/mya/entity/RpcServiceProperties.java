package org.whu.mya.entity;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcServiceProperties {

    private String group; // 当接口有多个实现时，用group区分
    private String serviceName;

    public String toRpcServiceName() {
        return this.getServiceName() + ":" + this.getGroup();
    }
}
