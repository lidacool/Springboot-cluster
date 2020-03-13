package com.lee.designPatterns.factory.abstractFac.color;

import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;

public class BlueCircle extends Circle {

    @Override
    public void draw() {
        {
            System.out.println("blue---");
            System.out.println(" /    \\");
            System.out.println("|      |");
            System.out.println(" \\    /");
            System.out.println("  ---");
        }
    }
}
