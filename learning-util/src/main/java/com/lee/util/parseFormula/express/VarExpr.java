package com.lee.util.parseFormula.express;

public class VarExpr extends Express {
    private final String varName;

    public VarExpr(String name) {
        this.varName = name;
    }

    @Override
    public double eval(VarsFetcher vars) {
        return vars.get(this.varName);
    }

    @Override
    public String toString() {
        return varName;
    }
}
