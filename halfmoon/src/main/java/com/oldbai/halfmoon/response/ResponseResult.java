package com.oldbai.halfmoon.response;

/**
 * 统一返回结果类
 *
 * @author 老白
 */
public class ResponseResult {

    private String message;
    private boolean success;
    private int code;
    private Object data;

    public ResponseResult(ResponseState commentResult) {
        this.message = commentResult.getMessage();
        this.success = commentResult.isSuccess();
        this.code = commentResult.getCode();
        this.data = null;
    }

    public static ResponseResult GET(ResponseState commentResult) {
        return new ResponseResult(commentResult);
    }

    public static ResponseResult GET(ResponseState commentResult, String message) {
        ResponseResult get = GET(commentResult);
        get.setMessage(message);
        return get;
    }


    public static ResponseResult JOIN_SUCCESS() {
        return new ResponseResult(ResponseState.JOIN_IN_SUCCESS);
    }

    public static ResponseResult NO_LOGIN() {
        return new ResponseResult(ResponseState.NOT_LOGIN);
    }

    public static ResponseResult NO_LOGIN(String message) {
        ResponseResult nologin = NO_LOGIN();
        nologin.setMessage(message);
        return nologin;
    }

    public static ResponseResult NO_PERMISSION() {
        return new ResponseResult(ResponseState.PERMISSION_DENIED);
    }

    public static ResponseResult NO_PERMISSION(String message) {
        ResponseResult noPermission = NO_PERMISSION();
        noPermission.setMessage(message);
        return noPermission;
    }


    public static ResponseResult SUCCESS() {
        return new ResponseResult(ResponseState.SUCCESS);
    }

    public static ResponseResult SUCCESS(String message) {
        ResponseResult success = SUCCESS();
        success.setMessage(message);
        return success;
    }

    public static ResponseResult SUCCESS(Object data) {
        ResponseResult success = SUCCESS();
        success.setData(data);
        return success;
    }

    public static ResponseResult SUCCESS(String message, Object data) {
        ResponseResult success = SUCCESS();
        success.setMessage(message);
        success.setData(data);
        return success;
    }

    public static ResponseResult FAILED() {
        return new ResponseResult(ResponseState.FAILED);
    }

    public static ResponseResult FAILED(String message) {
        ResponseResult failed = FAILED();
        failed.setMessage(message);
        return failed;
    }

    public static ResponseResult FAILED(Object data) {
        ResponseResult failed = FAILED();
        failed.setData(data);
        return failed;
    }

    public static ResponseResult FAILED(String message, Object data) {
        ResponseResult failed = FAILED();
        failed.setMessage(message);
        failed.setData(data);
        return failed;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}