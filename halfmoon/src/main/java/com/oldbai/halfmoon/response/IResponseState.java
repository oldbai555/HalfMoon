package com.oldbai.halfmoon.response;

/**
 * 枚举接口
 * @author 老白
 */
public interface IResponseState {

    String getMessage();

    boolean isSuccess();

    int getCode();
}
