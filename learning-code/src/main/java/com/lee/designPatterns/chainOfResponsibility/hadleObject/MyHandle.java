package com.lee.designPatterns.chainOfResponsibility.hadleObject;

import com.lee.designPatterns.chainOfResponsibility.AbstractHandle;
import com.lee.designPatterns.chainOfResponsibility.Handle;

public class MyHandle extends AbstractHandle implements Handle {

    private String name;
    private int handle = 0;

    public MyHandle(String name) {
        this.name = name;
    }

    @Override
    public void handle() {
        handle++;
//        if (handle == 50) {
            System.out.println(name + "handled!");
//            return;
//        }
        if (getHandle() != null)
            getHandle().handle();
    }
}
