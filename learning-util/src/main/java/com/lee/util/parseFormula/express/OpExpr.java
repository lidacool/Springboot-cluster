package com.lee.util.parseFormula.express;

import com.lee.util.parseFormula.ParseException;
import com.lee.util.parseFormula.operate.Operator;

public class OpExpr extends Express {

    protected Operator op;

    public OpExpr(Operator op, Express... children) {
        super(children);
        this.op = op;
    }

    @Override
    public double eval(VarsFetcher vars) {
        if(this.children == null) throw new ParseException(105, this.toString());

        return this.op.eval(vars, children);
    }

    @Override
    public String toString() {
        return op.toString(children);
    }

}
