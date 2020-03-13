package com.lee.readJson.util;

import com.google.common.collect.ListMultimap;

public abstract class JsonConfig {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JsonProperty [id=" + id + "]";
    }

    /**
     * 加载json时程序内部初始化处理并检查
     * consider abstract
     * */
    public void check(ListMultimap listMultimap){

    }
}
