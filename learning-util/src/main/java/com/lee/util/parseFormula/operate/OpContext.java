package com.lee.util.parseFormula.operate;

import com.lee.util.parseFormula.express.Express;
import com.lee.util.parseFormula.express.VarsFetcher;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import com.lee.util.parseFormula.operate.Operator.OP;


public class OpContext {

    private static EnumMap<OP, Operator> sm;

    private static Map<String, Operator> fm;

    static {
        fm = new HashMap<String, Operator>();
        fm.put("abs", new FuncOp("abs"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.abs(args[0].eval(vars));
            }});
        fm.put("exp", new FuncOp("exp"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.exp(args[0].eval(vars));
            }});
        fm.put("sqrt", new FuncOp("sqrt"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.sqrt(args[0].eval(vars));
            }});
        fm.put("sign", new FuncOp("sign"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Functions.sign(args[0].eval(vars));
            }});
        fm.put("log", new FuncOp("log"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.log(args[0].eval(vars));
            }});
        fm.put("log10", new FuncOp("log10"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.log10(args[0].eval(vars));
            }});
        fm.put("sin", new FuncOp("sin"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.sin(args[0].eval(vars));
            }});
        fm.put("cos", new FuncOp("cos"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.cos(args[0].eval(vars));
            }});
        fm.put("tan", new FuncOp("tan"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.tan(args[0].eval(vars));
            }});
        fm.put("atan", new FuncOp("atan"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.atan(args[0].eval(vars));
            }});
        fm.put("round", new FuncOp("round"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                if(args.length == 1) {
                    return Math.round(args[0].eval(vars));
                } else {
                    return Functions.round(args[0].eval(vars), args[1].eval(vars));
                }
            }});
        fm.put("ceil", new FuncOp("ceil"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                if(args.length == 1) {
                    return Math.ceil(args[0].eval(vars));
                } else {
                    return Functions.ceil(args[0].eval(vars), args[1].eval(vars));
                }
            }});
        fm.put("floor", new FuncOp("floor"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                if(args.length == 1) {
                    return Math.floor(args[0].eval(vars));
                } else {
                    return Functions.floor(args[0].eval(vars), args[1].eval(vars));
                }
            }});
        fm.put("pow", new FuncOp("pow"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.pow(args[0].eval(vars), args[1].eval(vars));
            }});
        fm.put("factorial", new FuncOp("factorial"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Functions.factorial(args[0].eval(vars));
            }});
        fm.put("min", new FuncOp("min"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.min(args[0].eval(vars), args[1].eval(vars));
            }
        });
        fm.put("max", new FuncOp("max"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.max(args[0].eval(vars), args[1].eval(vars));
            }
        });
        fm.put("rnd", new FuncOp("rnd") {
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args.length == 1 ?
                        Functions.random(args[0].eval(vars)) :
                        Functions.random(args[0].eval(vars), args[1].eval(vars));
            }
        });
        fm.put("random", new FuncOp("random") {
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args.length == 1 ?
                        Functions.random(args[0].eval(vars)) :
                        Functions.random(args[0].eval(vars), args[1].eval(vars));
            }
        });
        fm.put("if", new FuncOp("if"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) > 0 ? args[1].eval(vars) : args[2].eval(vars);
            }
        });


        sm = new EnumMap<OP, Operator>(OP.class);
        sm.put(OP.TERNARY, new ArithOp("?"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) > 0 ? args[1].eval(vars) : args[2].eval(vars);
            }
        });
        sm.put(OP.AND, new ArithOp("&"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return ((int)args[0].eval(vars) & (int)args[1].eval(vars));
            }
        });
        sm.put(OP.OR, new ArithOp("|"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return ((int)args[0].eval(vars) | (int)args[1].eval(vars));
            }
        });
        sm.put(OP.BITSHIFTLEFT, new ArithOp("<<"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return ((int)args[0].eval(vars) << (int)args[1].eval(vars));
            }
        });
        sm.put(OP.BITSHIFTRIGHT, new ArithOp(">>"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return ((int)args[0].eval(vars) >> (int)args[1].eval(vars));
            }
        });
        sm.put(OP.EQUAL, new ArithOp("="){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) == args[1].eval(vars)) ? 1 : 0;
            }
        });
        sm.put(OP.UNEQUAL, new ArithOp("<>"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) == args[1].eval(vars)) ? 0 : 1;
            }
        });
        sm.put(OP.SMALLER, new ArithOp("<"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) < args[1].eval(vars)) ? 1 : 0;
            }
        });
        sm.put(OP.LARGER, new ArithOp(">"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) > args[1].eval(vars)) ? 1 : 0;
            }
        });
        sm.put(OP.SMALLEREQ, new ArithOp("<="){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) <= args[1].eval(vars)) ? 1 : 0;
            }
        });
        sm.put(OP.LARGEREQ, new ArithOp(">="){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) >= args[1].eval(vars)) ? 1 : 0;
            }
        });
        sm.put(OP.PLUS, new ArithOp("+"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) + args[1].eval(vars);
            }
        });
        sm.put(OP.MINUS, new ArithOp("-"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) - args[1].eval(vars);
            }
        });
        sm.put(OP.MULTIPLY, new ArithOp("*"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) * args[1].eval(vars);
            }
        });
        sm.put(OP.DIVIDE, new ArithOp("/"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) / args[1].eval(vars);
            }
        });
        sm.put(OP.MODULUS, new ArithOp("%"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) % args[1].eval(vars);
            }
        });
        sm.put(OP.XOR, new ArithOp("^"){//unsupported now (^ used as pow)
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (int) args[0].eval(vars) ^ (int) args[1].eval(vars);
            }
        });
        sm.put(OP.POW, new ArithOp("^"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Math.pow(args[0].eval(vars), args[1].eval(vars));
            }
        });
        sm.put(OP.FACTORIAL, new ArithOp("!"){
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return Functions.factorial(args[0].eval(vars));
            }
        });
        sm.put(OP.LOGICAND, new ArithOp("&&") {
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) > 0 && args[1].eval(vars) > 0) ? 1 : 0;
            }
        });
        sm.put(OP.LOGICOR, new ArithOp("||") {
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return (args[0].eval(vars) > 0 || args[1].eval(vars) > 0) ? 1 : 0;
            }
        });
        sm.put(OP.LOGICNOT, new ArithOp("!") {//unsupported now
            @Override
            public double eval(VarsFetcher vars, Express[] args) {
                return args[0].eval(vars) > 0 ? 0 : 1;
            }
        });
    }

    public static Operator build(String fnName) {
        return fm.get(fnName.toLowerCase());
    }

    public static Operator build(OP opId) {
        return sm.get(opId);
    }

    public static boolean isFunc(String fnName) {
        return fm.containsKey(fnName.toLowerCase());
    }

}
