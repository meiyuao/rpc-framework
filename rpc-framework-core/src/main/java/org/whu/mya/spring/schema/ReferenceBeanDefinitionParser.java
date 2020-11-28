package org.whu.mya.spring.schema;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
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
        return ReferenceBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
////        builder.addPropertyValue("xx","dd");
//        String interfaces = element.getAttribute("interface");
//        Class clazz = null;
//        try {
//            clazz = Class.forName(interfaces);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
//
//        System.out.println(beanDefinition.getBeanClass());
////        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
//        System.out.println(beanDefinition.getConstructorArgumentValues());
//        beanDefinition.setBeanClass(clazz);
//        System.out.println(beanDefinition.getBeanClass());
//

        try {
            builder.addPropertyValue("clazz", Class.forName(element.getAttribute("interface")));
            builder.addPropertyValue("group", element.getAttribute("group"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//
//        builder.addPropertyValue("interfaceName", element.getAttribute("interface"));
//        builder.addPropertyValue("group", element.getAttribute("group"));
    }
}
