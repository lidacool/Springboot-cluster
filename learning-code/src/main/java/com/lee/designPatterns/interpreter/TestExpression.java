package com.lee.designPatterns.interpreter;

import com.lee.designPatterns.interpreter.impl.Divide;

import java.util.Enumeration;
import java.util.Vector;

public class TestExpression {

    public static void main(String[] args) {
        Expression expression=new Divide();
        System.out.println(expression.interpreter(new Context(4,2)));
        Vector v=new Vector(3,2);
        v.addElement(new String("hh"));
        v.addElement(new Integer(2));
        Enumeration enumeration=v.elements();
        while (enumeration.hasMoreElements())
            System.out.println(enumeration.nextElement());
    }
}
