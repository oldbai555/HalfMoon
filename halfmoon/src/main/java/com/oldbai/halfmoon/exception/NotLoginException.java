package com.oldbai.halfmoon.exception;

/**
 * 未登录异常处理
 *
 * @author 老白
 */
public class NotLoginException extends Exception {

    @Override
    public String toString() {
        return "not login.";
    }
}