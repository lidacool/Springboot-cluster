package com.lee.util.parseFormula.express;

public class BracketExpr extends Express {
    public BracketExpr(Express expr) {
        super(expr);
    }

    @Override
    public double eval(VarsFetcher vars) {
        return children[0].eval(vars);
    }

    @Override
    public String toString() {
        return "(" + children[0] + ")";
    }
}
