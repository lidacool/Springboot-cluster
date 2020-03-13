package com.lee.agent;

import com.lee.InjectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AgentComponent implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        List<Class<?>> list = ClassUtil.getClasses(Agent.class);
        for (Class<?> clazz : list) {
            RootBeanDefinition rootBeanDefinitio = new RootBeanDefinition(AgentBuilder.buildClass(clazz));
            rootBeanDefinitio.setTargetType(clazz);
            rootBeanDefinitio.setPrimary(true);
            rootBeanDefinitio.setRole(BeanDefinition.ROLE_APPLICATION);
            registry.registerBeanDefinition(clazz.getCanonicalName(), rootBeanDefinitio);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialAfterApplicationReady() {
        List<Class<?>> agentIntes = ClassUtil.getClasses(Agent.class);
        List<Class<?>> agentImpls = ClassUtil.getClasses(UserAgent.class);
        Collections.sort(agentImpls, (c1, c2) -> Integer.compare(getUserAgentOrder(c2), getUserAgentOrder(c1)));

        for (Class<?> agentInte : agentIntes) {
            List<Object> list = agentImpls.stream().filter(agentInte::isAssignableFrom).map(InjectUtil::getBean).collect(Collectors.toList());
            if (list == null || list.isEmpty()) {
                continue;
            }
            LinkedList agents = new LinkedList(list);
            AgentBuilder.setAgents(InjectUtil.getBean(agentInte), agents);
        }
    }

    private int getUserAgentOrder(Class<?> c2) {
        return c2.getAnnotation(UserAgent.class).value();
    }
}
