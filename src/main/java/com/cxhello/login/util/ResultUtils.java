package com.cxhello.login.util;

import com.cxhello.login.enums.StatusCodeEnum;

/**
 * @author cxhello
 * @date 2021/10/26
 */
public class ResultUtils {

    private ResultUtils() {

    }

    public static Result<Object> success() {
        return new Result<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getMsg());
    }

    public static Result<Object> fail(String msg) {
        return new Result<>(StatusCodeEnum.FAIL.getCode(), msg);
    }

    public static Result<Object> result(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> result(T data){
        return new Result<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getMsg(), data);
    }

}
