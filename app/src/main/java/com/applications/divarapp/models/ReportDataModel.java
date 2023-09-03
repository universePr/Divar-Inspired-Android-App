package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class ReportDataModel
{
    @SerializedName("ischat")
    private boolean IsChat;
    @SerializedName("isad")
    private boolean IsAd ;
    @SerializedName("desc")
    private String Description;
    @SerializedName("adid")
    private long AdId;
    @SerializedName("phone")
    private String UserPhone;

    public ReportDataModel(boolean isChat, boolean isAd, String description, long adId, String userPhone) {
        IsChat = isChat;
        IsAd = isAd;
        Description = description;
        AdId = adId;
        UserPhone = userPhone;
    }

    public boolean isChat() {
        return IsChat;
    }

    public void setChat(boolean chat) {
        IsChat = chat;
    }

    public boolean isAd() {
        return IsAd;
    }

    public void setAd(boolean ad) {
        IsAd = ad;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
