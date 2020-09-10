package com.wby.netty.server.im.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description
 * @Date 2020/9/7 19:15
 * @Author wuby31052
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    protected static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return context.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return context.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeans(Class<T> requiredType) {
        return context.getBeansOfType(requiredType);
    }

    public static boolean containsBean(String name) {
        return context.containsBean(name);
    }
}
