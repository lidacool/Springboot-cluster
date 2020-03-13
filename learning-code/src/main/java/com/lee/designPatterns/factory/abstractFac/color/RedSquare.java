package com.lee.designPatterns.factory.abstractFac.color;

import com.lee.designPatterns.factory.abstractFac.divByLv.Square;

public class RedSquare extends Square {

    @Override
    public void draw() {
        {
            System.out.println("R----");
            System.out.println("|    |");
            System.out.println("|    |");
            System.out.println("|    |");
            System.out.println(" ----");
        }
    }
}
