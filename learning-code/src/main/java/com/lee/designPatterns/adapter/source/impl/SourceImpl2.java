package com.lee.designPatterns.adapter.source.impl;

import com.lee.designPatterns.adapter.adapter.AbstractAdapter;

public class SourceImpl2 extends AbstractAdapter {

    @Override
    public void method2() {
        System.out.println("this is a implements one method2 of InterfaceSource class");
    }

    @Override
    public void method3() {
        System.out.println("this is a implements one method3 of InterfaceSource class");
    }


}
