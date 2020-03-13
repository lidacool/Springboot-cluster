package com.lee.exception;

import com.lee.util.string.StringUtil;

public class BusinessException extends RuntimeException {

    private String msg;
    private int code;

    public BusinessException(int code,String msg, Object...obj) {
        this.code = code;

        this.msg = StringUtil.format(msg, obj);
    }

    public BusinessException(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
