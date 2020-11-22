package org.whu.mya.spring.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistryConfig {

    private String address; // 接口权限名
    private int port; // 实现类全限定名
}
