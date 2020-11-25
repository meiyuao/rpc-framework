package org.whu.mya.spring.schema;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.whu.mya.spring.config.SerializeConfig;

public class SerializeBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected Class<?> getBeanClass(Element element) {
        return SerializeConfig.class;
    }


    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {

        builder.addPropertyValue("type", element.getAttribute("type"));
    }



}
