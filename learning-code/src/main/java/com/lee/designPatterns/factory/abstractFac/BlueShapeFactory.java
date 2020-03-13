package com.lee.designPatterns.factory.abstractFac;

import com.lee.designPatterns.factory.abstractFac.color.BlueCircle;
import com.lee.designPatterns.factory.abstractFac.color.BlueSquare;
import com.lee.designPatterns.factory.abstractFac.color.RedSquare;
import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;
import com.lee.designPatterns.factory.abstractFac.divByLv.Square;

public class BlueShapeFactory implements ShapeFactory {
    @Override
    public Circle createCircle() {
        return new BlueCircle();
    }

    @Override
    public Square createSquare() {
        return new BlueSquare();
    }
}
