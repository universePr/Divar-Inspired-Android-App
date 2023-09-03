package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class ResponseModel {
    @SerializedName("cd")
    private int Code ;
    @SerializedName("msg")
    private String Message;
    @SerializedName("tk")
    private String Token ;
    //@SerializedName("ex")
    //private Date Expires ;

    public int getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }

    public String getToken() {
        return Token;
    }


    public void setCode(int code) {
        Code = code;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setToken(String token) {
        Token = token;
    }
}
