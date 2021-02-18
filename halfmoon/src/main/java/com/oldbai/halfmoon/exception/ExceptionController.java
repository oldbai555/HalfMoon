package com.oldbai.halfmoon.exception;


import com.oldbai.halfmoon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 * @author 老白
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseResult handlerNotLoginException(AccessDeniedException e) {
        log.info(e.getMessage());
        e.printStackTrace();
        return ResponseResult.NO_PERMISSION("账号未登录或权限不足");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult handlerException(Exception e) {
        e.printStackTrace();
        log.info(e.getMessage());
        return ResponseResult.FAILED("服务器繁忙");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseResult handlerException(DataIntegrityViolationException e) {
        e.printStackTrace();
        log.info(e.getMessage());
        return ResponseResult.FAILED("请检查数据是否符合规则......");
    }
}