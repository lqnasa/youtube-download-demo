package com.onemt.swarm.enums;

/**
 * Created by Administrator on 2017/10/14 0014.
 */

public enum ResultCodeEnums {
    OK(0 , "成功"),
    ERROR(-1 , "失败")
    ;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码信息
     */
    private String msg;

    ResultCodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}