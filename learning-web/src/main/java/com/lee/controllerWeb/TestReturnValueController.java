package com.lee.controllerWeb;

import com.lee.service.TestAroundNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试返回结果json格式
 * 请求参数json格式
 * 地址访问方法
 * */
@RestController
@RequestMapping("/trv")
public class TestReturnValueController {

    @Autowired
    private TestAroundNotice testAroundNotice;

    @RequestMapping("/trvMethod")
    public Object testReturnValue() {

        String testStr3 = "[{\"name\":\"李岭左\",\"age\":12,\"sex\":\"男\"},{\"name\":\"司马懿\",\"age\":12,\"sex\":\"女\"}]";

        return testAroundNotice.testAroundNotice(testStr3);
    }


}
