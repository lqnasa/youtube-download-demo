package com.onemt.swarm.enums;

/**
 * 异常状态码
 * Created by Administrator on 2017/10/15 0015.
 */

public enum ExceptionCodeEnums {
    PARAM_ERROR(1 , "视频videoId参数不正确"),

    PLAY_ERROR(10 , "视频无法播放"),

    ANALYSIS_FAIL(11 , "视频下载地址解析失败");

    private Integer code;

    private String msg;

    ExceptionCodeEnums(Integer code, String msg) {
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
