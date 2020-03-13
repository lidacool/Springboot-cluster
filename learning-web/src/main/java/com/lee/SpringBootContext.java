package com.lee;

import com.lee.invoke.InvokerMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SpringBootContext implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        InjectUtil.init(applicationContext);
        applicationContext.getBean(InvokerMap.class).initial(applicationContext, Service.class);
//		Constant.load(applicationContext.getBean(Conf.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
