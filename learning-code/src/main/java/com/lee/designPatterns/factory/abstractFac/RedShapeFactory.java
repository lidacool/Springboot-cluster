package com.lee.designPatterns.factory.abstractFac;


import com.lee.designPatterns.factory.abstractFac.color.RedCircle;
import com.lee.designPatterns.factory.abstractFac.color.RedSquare;
import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;
import com.lee.designPatterns.factory.abstractFac.divByLv.Square;

public class RedShapeFactory implements ShapeFactory {


    @Override
    public Circle createCircle() {
        return new RedCircle();
    }

    @Override
    public Square createSquare() {
        return new RedSquare();
    }
}
