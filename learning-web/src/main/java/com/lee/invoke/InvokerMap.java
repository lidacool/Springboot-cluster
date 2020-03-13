package com.lee.invoke;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvokerMap {

    private static Map<String,Object> serviceObjMap = new HashMap<>();

    public void initial(ApplicationContext ctx, Class<? extends Annotation> annotationType) {
         this.serviceObjMap = ctx.getBeansWithAnnotation(annotationType);
    }

    public static Object getObject(String service_name) {
        return serviceObjMap.get(service_name);
    }

}
