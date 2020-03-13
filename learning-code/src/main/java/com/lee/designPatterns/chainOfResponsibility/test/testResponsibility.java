package com.lee.designPatterns.chainOfResponsibility.test;

import com.lee.designPatterns.chainOfResponsibility.hadleObject.MyHandle;

public class testResponsibility {

    public static void main(String[] args) {

        MyHandle handle1=new MyHandle("handle1");
        MyHandle handle2=new MyHandle("handle2");
        MyHandle handle3=new MyHandle("handle3");

        handle1.setHandle(handle2);
        handle2.setHandle(handle3);
//        handle3.setHandle(handle1);

        handle1.handle();
    }
}
