package org.whu.mya.spring.beanParser;

import javafx.concurrent.Service;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.whu.mya.entity.RpcServiceProperties;
import org.whu.mya.factory.SingletonFactory;
import org.whu.mya.provider.SerivceProviderImpl;
import org.whu.mya.provider.ServiceProvider;

import java.lang.reflect.InvocationTargetException;


public class ServiceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(SerivceProviderImpl.class);

    @Override
    protected Class<?> getBeanClass(Element element) {
        try {
            return ClassLoader.getSystemClassLoader()
                    .loadClass(element.getAttribute("ref"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        System.out.println(builder.getBeanDefinition().getBeanClass());
        try {
            Object service = ClassLoader.getSystemClassLoader()
                    .loadClass(element.getAttribute("ref")).getDeclaredConstructor().newInstance();

            serviceProvider.publishService(service,
                    RpcServiceProperties.builder().group(element.getAttribute("group")).build());

        }catch (Exception e) {
            throw new RuntimeException();
        }

        System.out.println(element.getAttribute("group"));
    }

}
