package com.lee.requestOp;

import com.alibaba.fastjson.JSON;
import com.lee.util.log.Logging;

public class JsonRequestVo {

    private String service_name;
    private String method_name;
    private String[] parame;

    public Object parseArg(int i, Class<?> type) throws Exception {
        // 兼容接口声明的方法参数中的原始类型（int、long）和封装对象类型（Integer、Long）
        if (type == long.class) {
            return Long.valueOf(parame[i]);
        } else if (type == int.class) {
            return Integer.valueOf(parame[i]);
        } else if (type == boolean.class) {
            return Boolean.valueOf(parame[i]);
        } else if (type == String.class) {
            return parame[i];
        } else if (Enum.class.isAssignableFrom(type)) {
            return type.getMethod("valueOf", String.class).invoke(null, parame[i]);
        } else {
            return JSON.parseObject(parame[i], type);
        }
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getService_name() {
        return service_name;
    }

    public String getMethod_name() {
        return method_name;
    }

    public String[] getParame() {
        return parame;
    }

    public void setParame(String[] parame) {
        this.parame = parame;
    }
}
