package org.whu.mya.spring.schema;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.ServiceProviderImpl;
import org.whu.mya.provider.ServiceProvider;
import org.whu.mya.spring.config.ServiceBean;


public class ServiceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServiceBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        try {
//            Object service = ClassLoader.getSystemClassLoader()
//                    .loadClass(element.getAttribute("ref")).getDeclaredConstructor().newInstance();
//
//            serviceProvider.publishService(service,
//                    RpcServiceProperties.builder().group(element.getAttribute("group")).build());

            builder.addPropertyValue("interfaceName", element.getAttribute("interface"));
            builder.addPropertyValue("ref", element.getAttribute("ref"));
            builder.addPropertyValue("group", element.getAttribute("group"));
//            builder.addPropertyValue("service", service);

        }catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
