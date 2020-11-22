package org.whu.mya.spring.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBean {

    private String interfaceName; // 接口权限名
    private String ref; // 实现类全限定名
    private String group; // 分组名
    private Object service; // 指向服务实例
}
