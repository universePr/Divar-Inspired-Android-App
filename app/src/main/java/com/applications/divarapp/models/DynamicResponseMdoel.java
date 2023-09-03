package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class DynamicResponseMdoel {
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
