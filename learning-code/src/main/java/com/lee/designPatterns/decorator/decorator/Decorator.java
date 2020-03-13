package com.lee.designPatterns.decorator.decorator;

import com.lee.designPatterns.decorator.Source;
import com.lee.designPatterns.decorator.impl.SourceInpl;

public class Decorator implements Source {

    private SourceInpl sourceInpl;

    public Decorator(SourceInpl sourceInpl) {//装饰模式，需要调用者new instance
        this.sourceInpl = sourceInpl;
    }

    public Decorator( ) {//代理模式，不需要调用者new instance
        this.sourceInpl = new SourceInpl();
    }

    @Override
    public void method() {
        System.out.print("before decorate,");
        sourceInpl.method();
        System.out.print("complete decorate.");
    }
}
