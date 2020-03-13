package com.lee.designPatterns.factory.abstractFac.color;

import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;

public class RedCircle extends Circle {

    @Override
    public void draw() {
        {
            System.out.println("red---");
            System.out.println(" /    \\");
            System.out.println("|      |");
            System.out.println(" \\    /");
            System.out.println("  ---");
        }
    }
}
