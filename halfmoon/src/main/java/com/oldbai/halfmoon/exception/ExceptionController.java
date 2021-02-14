package com.oldbai.halfmoon.exception;


import com.oldbai.halfmoon.response.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 * @author 老白
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public ResponseResult handlerNotLoginException(NotLoginException e) {
        e.printStackTrace();
        return ResponseResult.FAILED("账号未登录");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult handlerException(Exception e) {
        e.printStackTrace();
        return ResponseResult.FAILED("服务器繁忙");
    }

}