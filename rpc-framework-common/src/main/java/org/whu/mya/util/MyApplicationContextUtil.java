package org.whu.mya.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyApplicationContextUtil {

    private static ApplicationContext context;

    //声明一个静态变量保存

    public static void setApplicationContext(ApplicationContext contex) throws BeansException {
        context = contex;
    }
    public static ApplicationContext getContext(){
        return context;
    }
    public final static Object getBean(String beanName){
        if (context == null) return null;
        return context.getBean(beanName);
    }

    public final static Object getBean(String beanName, Class<?> requiredType) {
        return context.getBean(beanName, requiredType);
    }
}
