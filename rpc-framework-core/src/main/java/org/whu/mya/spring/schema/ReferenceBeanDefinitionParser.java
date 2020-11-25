package org.whu.mya.spring.schema;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.ServiceProvider;
import org.whu.mya.provider.ServiceProviderImpl;
import org.whu.mya.proxy.RpcClientProxy;
import org.whu.mya.spring.config.ReferenceBean;
import org.whu.mya.spring.config.ServiceBean;

import java.lang.reflect.Proxy;


public class ReferenceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        System.out.println("hahah");
        return Object.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.addPropertyValue("xx","dd");
//        builder.addPropertyValue("interfaceName", element.getAttribute("interface"));
//        builder.addPropertyValue("group", element.getAttribute("group"));
    }
}
