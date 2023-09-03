package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class CheckChatDataModel {
    @SerializedName("aid")
    private long AdId ;
    @SerializedName("uph")
    private String UserPhone;

    public CheckChatDataModel(long adId, String userPhone) {
        AdId = adId;
        UserPhone = userPhone;
    }

    public long getAdId() {
        return AdId;
    }

    public void setAdId(long adId) {
        AdId = adId;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
