package com.lee.designPatterns.factory.abstractFac;

import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;
import com.lee.designPatterns.factory.abstractFac.divByLv.Square;

public class Test {

    /**
     * eg: 在有方圆棱三个形状的产品上 ， 增加红绿蓝3个颜色 需创建的工厂就很多
     * <p>
     * impl:将一个形状当做一个等级 一种颜色当成族
     */

    public static void main(String s[]) {
        ShapeFactory shapeFactory = new RedShapeFactory();

        Circle circle = shapeFactory.createCircle();

        ShapeFactory shapeFactory2 = new BlueShapeFactory();

        Square square = shapeFactory2.createSquare();

        circle.draw();
        square.draw();

    }

}
