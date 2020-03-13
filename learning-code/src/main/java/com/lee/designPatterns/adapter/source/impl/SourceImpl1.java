package com.lee.designPatterns.adapter.source.impl;

import com.lee.designPatterns.adapter.adapter.AbstractAdapter;

public class SourceImpl1 extends AbstractAdapter {

    @Override
    public void method1() {
        System.out.println("this is a implements one method1 of InterfaceSource class");
    }
}
