package com.lee.designPatterns.factory.abstractFac.color;

import com.lee.designPatterns.factory.abstractFac.divByLv.Square;

public class BlueSquare extends Square {
    @Override
    public void draw() {
        {
            System.out.println("B----");
            System.out.println("|    |");
            System.out.println("|    |");
            System.out.println("|    |");
            System.out.println(" ----");
        }
    }
}
