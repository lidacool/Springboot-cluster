package com.lee.designPatterns.decorator.impl;

import com.lee.designPatterns.decorator.Source;

public class SourceInpl implements Source {
    @Override
    public void method() {
        System.out.println("i am a implements calss of source");
    }
}
