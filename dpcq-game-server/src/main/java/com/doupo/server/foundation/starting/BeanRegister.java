package com.doupo.server.foundation.starting;

import org.gaming.fakecmd.side.game.GameCmdManager;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleSupport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Component
public class BeanRegister implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(
            Object bean, String beanName) throws BeansException {

        if (bean instanceof Lifecycle) {
            LifecycleSupport.registerBean((Lifecycle) bean);
        }

        if (bean.getClass().getAnnotation(Controller.class) != null) {
            GameCmdManager.registerBean(bean);
        }

        if (bean.getClass().getAnnotation(Service.class) != null
                || bean.getClass().getAnnotation(Component.class) != null) {
            EventBus.register(bean);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean, String beanName) throws BeansException {
        return bean;
    }
}