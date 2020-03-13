package com.lee.service;

import com.lee.agent.TestAgent;
import com.lee.agent.UserAgent;

import java.util.Map;

@UserAgent
public interface TestAroundNotice extends TestAgent {

    Map<String,Object> testAroundNotice(String testStr3);

    Map<String,Object> updateSysTime(long time);

    Map<String,Object> testJRedis(String key,String value);

    Map<String,Object> timerStart();

    Map<String,Object> testJson();

    Map<String,Object> testJsonParse();
}
