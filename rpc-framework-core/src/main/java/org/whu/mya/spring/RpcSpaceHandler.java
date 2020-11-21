package org.whu.mya.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.whu.mya.spring.beanParser.ServiceBeanDefinitionParser;

public class RpcSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParser());
    }
}
