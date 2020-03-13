package com.lee.util.parseFormula.express;

public class VarsExpr {

    private final Express expr;

    private InnerVarsFetcher varsFetcher;

    public VarsExpr(Express expr) {
        this.expr = expr;
    }

    public VarsExpr setVar(String name, double var) {
        if(expr.isConst()) return this;//常量不需要设置参数, 不做无用功

        if(varsFetcher == null) {
            varsFetcher = new InnerVarsFetcher();
        }
        varsFetcher.setVar(name, var);
        return this;
    }

    public double eval() {
        return expr.eval(varsFetcher);
    }

    private class InnerVarsFetcher implements VarsFetcher {//鉴于一般elements数据均较少 直接用LinkedList
        private VarElement head = null;
        private VarElement tail = null;
        public void setVar(String name, double var) {
            VarElement element = new VarElement(name, var);
            if(tail == null) {
                tail = head = element;
            } else {
                tail = tail.next = element;
            }
        }
        @Override
        public double get(String name) {
            VarElement element = head;
            while(element != null) {
                if(element.name.equals(name)) return element.value;
                element = element.next;
            }
            return 0;
        }
    }
    private class VarElement {
        final String name;
        final double value;
        VarElement next;
        public VarElement(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }

}
