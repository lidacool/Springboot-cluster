package com.lee.controllerWeb;

import com.lee.exception.BusinessException;
import com.lee.invoke.InvokerMap;
import com.lee.requestOp.JsonRequestVo;
import com.lee.util.log.Logging;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 接口访问
 */
@Controller
public class IndexController {

    @PostMapping("/dispatch.sl")
    @ResponseBody
    public Object dispatch(@RequestBody JsonRequestVo request) throws Exception {
        Object object = invoke(request);
        return object;
    }

    public Object invoke(JsonRequestVo req) throws Exception {

        Object serviceObj = InvokerMap.getObject(req.getService_name());
        if(Objects.isNull(serviceObj)){
            throw new BusinessException(-2,"没找到该service-name:"+req.getService_name());
        }
        Class<?> serviceClass = serviceObj.getClass();

        FastClass fastClass = FastClass.create(serviceClass);

        Method[] methods = serviceClass.getDeclaredMethods();
        Method method = null;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(req.getMethod_name())) {
                method = methods[i];
            }
        }

        FastMethod fastMethod = fastClass.getMethod(method.getName(), method.getParameterTypes());

        Class[] argTypes = fastMethod.getParameterTypes();

        Object[] args = new Object[argTypes.length];


        if (req.getParame().length != args.length) {
            Logging.error("inteface need parame number:{},actual number:{}", args.length, req.getParame().length);
            throw new RuntimeException("interface parame number not matching exception!");
        }
        for (int i = 0; i < args.length; i++) {
            args[i] = req.parseArg(i, argTypes[i]);
        }
        return fastMethod.invoke(serviceObj, args);
    }

}
