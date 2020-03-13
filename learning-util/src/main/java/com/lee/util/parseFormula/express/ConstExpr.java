package com.lee.util.parseFormula.express;

import com.lee.util.parseFormula.ParseException;

import java.text.DecimalFormat;

public class ConstExpr extends Express {
    private final double value;
    private final String name;

    public ConstExpr(double value) {
        this(null, value);
    }

    public ConstExpr(String name, double value) {
        super();
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean isConst() {
        return true;
    }

    public static final boolean isConst(String name) {
        if("PI".equalsIgnoreCase(name)) {
            return true;
        }
        if("E".equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }

    public static final ConstExpr buildByName(String name) {
        if("PI".equalsIgnoreCase(name)) {
            return new ConstExpr("PI", Math.PI);
        }
        if("E".equalsIgnoreCase(name)) {
            return new ConstExpr("E", Math.E);
        }
        throw new ParseException(106, name);
    }

    @Override
    public String toString() {
        return name == null ? new DecimalFormat().format(value) : name;
    }

    @Override
    public double eval(VarsFetcher fecther) {
        return this.value;
    }

}
