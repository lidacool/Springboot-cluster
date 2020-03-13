package com.lee.designPatterns.bridge.impl;

import com.lee.designPatterns.bridge.DataLibrary;

public class MysqlData implements DataLibrary {
    @Override
    public void connectData() {
        System.out.println("welcome connect to mysql!");
    }
}
