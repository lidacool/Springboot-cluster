package com.lee.designPatterns.adapter.adapter;

import com.lee.designPatterns.adapter.source.Source;
import com.lee.designPatterns.adapter.targetInterface.Target;

/*对象适配器*/
public class ObjectAdapter implements Target {

    private Source source;

    public ObjectAdapter(Source source) {
        this.source = source;
    }

    @Override
    public void waitAdapterMethod() {
        source.waitAdapterMethod();
    }

    @Override
    public void myselfMethod() {
        System.out.println("this is target method!");
    }
}
