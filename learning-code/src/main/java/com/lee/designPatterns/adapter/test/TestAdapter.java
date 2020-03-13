package com.lee.designPatterns.adapter.test;

import com.lee.designPatterns.adapter.adapter.ClassAdapter;
import com.lee.designPatterns.adapter.adapter.ObjectAdapter;
import com.lee.designPatterns.adapter.source.InterfaceSource;
import com.lee.designPatterns.adapter.source.Source;
import com.lee.designPatterns.adapter.source.impl.SourceImpl1;
import com.lee.designPatterns.adapter.source.impl.SourceImpl2;
import com.lee.designPatterns.adapter.targetInterface.Target;

public class TestAdapter {

    public static void main(String[] args) {
        classAdapter();//类的适配器
        objectAdapter();//对象适配器
        interfaceAdapter();//接口适配器
    }

    private static void interfaceAdapter() {
        InterfaceSource interfaceSource=new SourceImpl1();
//        interfaceSource.method1();
//        interfaceSource.method2();
//        interfaceSource.method3();
        InterfaceSource interfaceSource1=new SourceImpl2();
        interfaceSource1.method1();
        interfaceSource1.method2();
        interfaceSource1.method3();
    }

    private static void objectAdapter() {
        Source source=new Source();
        Target target=new ObjectAdapter(source);
        target.waitAdapterMethod();
        target.myselfMethod();
    }

    private static void classAdapter() {
        Target target=new ClassAdapter();
        target.myselfMethod();
        target.waitAdapterMethod();
    }
}
