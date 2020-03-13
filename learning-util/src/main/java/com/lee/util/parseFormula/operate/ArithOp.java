package com.lee.util.parseFormula.operate;

import com.lee.util.parseFormula.express.Express;

public abstract  class ArithOp implements Operator {
    private final String op;

    public ArithOp(String op) {
        this.op = op;
    }

    @Override
    public String toString(Express[] children) {
        if(children.length == 1) {
            return ("-".equals(op) || "!".equals(op)) ? (op + children[0]) : (children[0] + op);//- !
        }
        if(children.length == 2) {
            return children[0] + " " + op + " " + children[1];
        }
        return children[0] + " " + op + " " + children[1] + ":" + children[2];//?:
    }
}
