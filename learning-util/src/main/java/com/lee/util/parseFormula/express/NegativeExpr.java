package com.lee.util.parseFormula.express;

public class NegativeExpr extends Express {
    public NegativeExpr(Express expr) {
        super(expr);
    }

    @Override
    public double eval(VarsFetcher vars) {
        return - children[0].eval(vars);
    }

    @Override
    public String toString() {
        return "-" + children[0];
    }
}
