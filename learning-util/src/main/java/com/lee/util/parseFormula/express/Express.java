package com.lee.util.parseFormula.express;

public abstract class Express {

    protected Express[] children;

    public Express(Express... children) {
        this.children = children;
    }

    public boolean isConst() {
        return false;
    }

    public VarsExpr toVarsExpr() {
        return new VarsExpr(this);
    }

    public abstract double eval(VarsFetcher vars);
}
