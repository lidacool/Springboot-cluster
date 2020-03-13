package com.lee.designPatterns.adapter.adapter;

import com.lee.designPatterns.adapter.source.Source;
import com.lee.designPatterns.adapter.targetInterface.Target;

/*类的适配器*/
public class ClassAdapter extends Source implements Target {
    @Override
    public void myselfMethod() {
        System.out.println("this is target method!");
    }
}
