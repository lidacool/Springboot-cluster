package com.lee.designPatterns.interpreter.impl;

import com.lee.designPatterns.interpreter.Context;
import com.lee.designPatterns.interpreter.Expression;

public class Divide implements Expression {
    @Override
    public int interpreter(Context context) {
        return context.getX()/context.getY();
    }
}
