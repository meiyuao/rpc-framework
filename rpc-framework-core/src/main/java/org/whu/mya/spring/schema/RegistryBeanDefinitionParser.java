package org.whu.mya.spring.schema;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.ServiceProvider;
import org.whu.mya.provider.ServiceProviderImpl;
import org.whu.mya.spring.config.RegistryConfig;

public class RegistryBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return RegistryConfig.class;
    }


    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        builder.addPropertyValue("address", element.getAttribute("address"));
        builder.addPropertyValue("port", element.getAttribute("port"));
    }



}
