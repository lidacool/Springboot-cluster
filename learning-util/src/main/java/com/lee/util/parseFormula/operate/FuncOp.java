package com.lee.util.parseFormula.operate;

import com.lee.util.parseFormula.express.Express;

public abstract class FuncOp implements Operator {

    private final String op;

    public FuncOp(String op) {
        this.op = op;
    }

    @Override
    public String toString(Express[] children) {
        StringBuilder sb = new StringBuilder().append(op).append("(");
        for (Express expr : children) {
            sb.append(expr).append(",");
        }
        return sb.delete(sb.length()-1, sb.length()).append(")").toString();
    }
}
