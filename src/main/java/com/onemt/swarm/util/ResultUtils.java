package com.onemt.swarm.util;


import com.onemt.swarm.enums.ExceptionCodeEnums;
import com.onemt.swarm.enums.ResultCodeEnums;
import com.onemt.swarm.vo.ResultVO;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ResultUtils {

    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ResultCodeEnums.OK.getCode());
        resultVO.setData(data);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code , String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return resultVO;
    }

    public static ResultVO error(ExceptionCodeEnums exceptionCodeEnums) {
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(exceptionCodeEnums.getMsg());
        resultVO.setCode(exceptionCodeEnums.getCode());
        return resultVO;
    }


    public static ResultVO error(ResultCodeEnums resultCodeEnums) {
        ResultVO resultVO = new ResultVO();
        resultVO.setMsg(resultCodeEnums.getMsg());
        resultVO.setCode(resultCodeEnums.getCode());
        return resultVO;
    }
}
