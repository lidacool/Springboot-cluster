package com.lee.readJson.testJson;

import com.google.common.collect.ListMultimap;
import com.lee.readJson.util.JsonConfig;

public class ConstJson extends JsonConfig {

    public String desc;
    public String name;
    public String value;

    @Override
    public void check(ListMultimap listMultimap) {
        super.check(listMultimap);

        for (ParseJson type:ParseJson.values()) {
            if (type.name().equals(this.name)){
                type.apply(value);
                System.out.println("exchange "+desc);
                break;
            }
        }
    }
}
