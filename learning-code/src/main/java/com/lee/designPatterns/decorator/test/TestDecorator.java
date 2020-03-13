package com.lee.designPatterns.decorator.test;

import com.lee.designPatterns.decorator.Source;
import com.lee.designPatterns.decorator.decorator.Decorator;
import com.lee.designPatterns.decorator.impl.SourceInpl;

public class TestDecorator {

    public static void main(String[] args) {
        //装饰
        SourceInpl sourceInpl=new SourceInpl();
        Source source=new Decorator(sourceInpl);
        source.method();
        //代理
        Source sourceProxy=new Decorator();
        sourceProxy.method();
    }
}
