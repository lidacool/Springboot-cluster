package com.lee.readJson.testJson;

import com.lee.util.parseFormula.Parser;
import com.lee.util.parseFormula.express.Express;
import com.lee.util.string.StringUtil;

import java.util.List;
import java.util.function.Function;

public enum ParseJson {

    test1,
    test2,
    test3(StringUtil::splitAsIntList),
    test4(StringUtil::splitAsInt),
    test5(s -> s),
    ;

    private final Function<String, Object> function;
    private Object object;

    ParseJson() {
        this(s -> StringUtil.isNumber(s) ? Integer.parseInt(s) : new Parser().parse(s));
    }

    ParseJson(Function<String, Object> function) {
        this.function = function;
    }

    public void apply(String s) {
        this.object = this.function.apply(s);
    }

    public Object get() {
        return object;
    }

    public int getAsInt() {
        return (int) object;
    }

    public int[] getValueAsIntArray() {
        return (int[]) object;
    }

    public List<int[]> getValueAsIntList() {
        return (List<int[]>) object;
    }

    public Express getValueAsExpr() {
        return (Express) object;
    }
}
