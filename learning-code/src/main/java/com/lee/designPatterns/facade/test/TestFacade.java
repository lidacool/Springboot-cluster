package com.lee.designPatterns.facade.test;

import com.lee.designPatterns.facade.Facade;

public class TestFacade {

    public static void main(String[] args) {
        Facade facade=new Facade();
        facade.startComputer();
        facade.shutDownComputer();
    }
}
