package com.lee.designPatterns.bridge.test;

import com.lee.designPatterns.bridge.bridge.Bridge;
import com.lee.designPatterns.bridge.bridge.MyBridge;
import com.lee.designPatterns.bridge.impl.MysqlData;
import com.lee.designPatterns.bridge.impl.OracleData;

public class TestBridge {

    public static void main(String[] args) {
        Bridge bridge=new MyBridge();
        bridge.setDataLibrary(new MysqlData());
        bridge.connectData();

        bridge.setDataLibrary(new OracleData());
        bridge.connectData();
    }
}
