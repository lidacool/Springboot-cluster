package com.lee.designPatterns.bridge.bridge;

public class MyBridge extends Bridge {

    @Override
    public void connectData() {
        getDataLibrary().connectData();
    }
}
