package com.lee.designPatterns.factory.abstractFac;

import com.lee.designPatterns.factory.abstractFac.divByLv.Circle;
import com.lee.designPatterns.factory.abstractFac.divByLv.Square;

public interface ShapeFactory {

    Circle createCircle();

    Square createSquare();
}
