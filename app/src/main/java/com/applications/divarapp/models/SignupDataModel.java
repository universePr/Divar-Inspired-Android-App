package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class SignupDataModel {
    @SerializedName("cd")
    private final int Code;
    @SerializedName("ph")
    private final String Phone;
    @SerializedName("ci")
    private final int CityId;

    public SignupDataModel(int code, String phone, int cityId) {
        Code = code;
        Phone = phone;
        CityId = cityId;
    }
}
