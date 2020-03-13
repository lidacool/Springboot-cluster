package com.lee.util.parseFormula.operate;

import com.lee.util.parseFormula.express.Express;
import com.lee.util.parseFormula.express.VarsFetcher;

public interface Operator {


    public static enum OP {
        UNKNOWN,
        TERNARY, //level 1
        AND, OR, XOR,   // level 2
        LOGICAND, LOGICOR, // level 3
        EQUAL, UNEQUAL, SMALLER, LARGER, SMALLEREQ, LARGEREQ, // level 4
        LOGICNOT,  // level 5
        BITSHIFTLEFT, BITSHIFTRIGHT, // level 6
        PLUS, MINUS, // level 7
        MULTIPLY, DIVIDE, MODULUS, // level 8
        POW, // level 9
        FACTORIAL, // level 10
    }

    public double eval(VarsFetcher vars, Express[] args);

    public String toString(Express[] children);

}
