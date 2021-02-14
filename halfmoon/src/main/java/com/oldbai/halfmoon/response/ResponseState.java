package com.oldbai.halfmoon.response;

/**
 * 枚举的实现
 *
 * @author 老白
 */

public enum ResponseState implements IResponseState {
    SUCCESS(10000, true, "操作成功"),
    FAILED(20000, false, "操作失败"),
    JOIN_IN_SUCCESS(20001, false, "注册成功"),
    PARAMS_ILL(30000, false, "参数错误"),
    PERMISSION_DENIED(40002, false, "权限不够"),
    ACCOUNT_FORBID(40003, false, "账号被禁用"),
    NOT_LOGIN(50000, false, "账号未登录"),
    ERROE_403(50403, false, "权限不足喔......"),
    ERROE_404(50404, false, "页面找不到了，页面丢失......"),
    ERROE_504(50504, false, "系统繁忙，稍后再试......"),
    ERROE_505(50505, false, "请求错误呀请检查数据是否正确......"),
    LOGIN_SUCCESS(60000, true, "登录成功");


    int code;
    boolean isSuccess;
    String message;

    ResponseState(int code, boolean isSuccess, String message) {
        this.code = code;
        this.isSuccess = isSuccess;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public int getCode() {
        return code;
    }

}