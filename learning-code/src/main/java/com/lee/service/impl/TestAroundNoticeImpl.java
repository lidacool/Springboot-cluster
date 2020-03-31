package com.lee.service.impl;

import com.lee.annotation.Person;
import com.lee.event.Container;
import com.lee.event.entity.TestEventEntity;
import com.lee.readJson.testJson.ConstData;
import com.lee.readJson.testJson.ParseJson;
import com.lee.service.TestAroundNotice;
import com.lee.timer.one.TimerTask;
import com.lee.util.jredis.JRedis;
import com.lee.util.json.JsonUtil;
import com.lee.util.log.Logging;
import com.lee.util.string.StringUtil;
import com.lee.util.time.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("testAroundNotice")
public class TestAroundNoticeImpl implements TestAroundNotice {


    @Autowired
    private JRedis jRedis;
    @Autowired
    private Container container;
    @Autowired
    private ConstData constData;

    @Override
    public Map<String, Object> testAroundNotice(String testStr3) {
        Map<String, Object> result = new HashMap<>();
        List<Person> persons = JsonUtil.fromJSONToList(testStr3, Person.class);
        result.put("persons",persons);
        return result;
    }

    @Override
    public Map<String, Object> updateSysTime(long time) {
        long now = TimeUtil.currentTimeMillis();
        Logging.info(" before update time {}", now);
        TimeUtil.changeCurrentTime(time);
        Logging.info(" after update time {}", TimeUtil.currentTimeMillis());

        Map<String, Object> result = new HashMap<>();
        result.put("nowTime", TimeUtil.currentTimeMillis());
        return result;
    }

    @Override
    public Map<String, Object> testJRedis(String key, String value) {

        Assert.isTrue(StringUtil.isNumber(value), "value is not number!");

        Map<String, Object> result = new HashMap<>();

        byte[] bytes = jRedis.get(key);

        result.put("old " + key, bytes == null ? "null" : JRedis.bytes2long(bytes));

        jRedis.set(key, JRedis.long2bytes(Long.parseLong(value)));

        result.put(key, JRedis.bytes2long(jRedis.get(key)));
        container.post(new TestEventEntity(StringUtil.format("比如：此时修改的是redis的key值为：{},在此之后可以进行一些此值变化后的异步操作", value)));
        return result;
    }

    @Override
    public Map<String, Object> timerStart() {
        TimerTask.start();
        return null;
    }

    @Override
    public Map<String, Object> testJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("const", constData.values());
        return result;
    }

    @Override
    public Map<String, Object> testJsonParse() {
        Map<String, Object> result = new HashMap<>();
        result.put("test1", ParseJson.test1.getAsInt());
        result.put("test2",ParseJson.test2.getValueAsExpr().eval(s->{
            if (s.equals("a")){
                return 1;
            }else if (s.equals("b")){
                return 2;
            }else if (s.equals("c")){
                return 3;
            }else if (s.equals("d")){
                return 4;
            }
            return 0;
        }));
        result.put("test3",ParseJson.test3.getValueAsIntList());
        result.put("test4",ParseJson.test4.getValueAsIntArray());
        result.put("test5",ParseJson.test5.get());
        return result;
    }

    @Override
    public void test() {
        Logging.info("test agent");
    }
}
