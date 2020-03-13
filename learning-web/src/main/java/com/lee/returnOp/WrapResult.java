package com.lee.returnOp;

import com.lee.util.log.Logging;
import com.lee.util.net.NetUtil;

import java.util.HashMap;
import java.util.Map;

public class WrapResult {

    public static Map<String, Object> newRespDataMap(int status, Object data, long begin) {
        Map<String, Object> resp = new HashMap<>(8);
        resp.put("status", status);
        resp.put("data", data);
        addServerTime(resp, begin);
        return resp;
    }

    public static void addServerTime(Map<String, Object> result, long begin) {
        long now = System.currentTimeMillis();
        result.put("t", now);
        Logging.info("{},consume time: {}ms",
                NetUtil.findClientIPStr(),
                (now - begin));
    }
}
