package com.credithc.hhr.library_common.bean;

import android.text.TextUtils;

import com.credithc.hhr.library_common.utils.ServerCode;


/**
 * Created by haoran on 2017/11/2.
 */

public class BaseResult<T> {


    T data;
    String code;
    String message;


    public T getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public boolean isSuccess() {
        return TextUtils.equals(ServerCode.CODE_200,code);
    }
}
