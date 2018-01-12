package com.onemt.swarm.vo;

import java.io.Serializable;

/**
 * 返回给前端的数据封装
 * Created by Administrator on 2017/10/13 0013.
 */
public class ResultVO<T> implements Serializable{
    private static final long serialVersionUID = -4414926978700453869L;
    /** 状态码 0 表示成功 */
    private Integer code;

    /** 状态码说明 */
    private String msg;

    /** 返回数据*/
    private T data;


    public ResultVO() {
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}