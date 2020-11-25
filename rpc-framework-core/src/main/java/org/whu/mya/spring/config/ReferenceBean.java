package org.whu.mya.spring.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceBean {

    private String interfaceName; // 接口权限名
    private String group;

}
