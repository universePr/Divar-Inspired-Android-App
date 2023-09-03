package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class ChatResponseCheck {
    @SerializedName("res")
    private boolean response;
    @SerializedName("adId")
    private long AdId;
    @SerializedName("adTitle")
    private String AdTitle ;
    @SerializedName("col")
    private String CollectionName;


    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public long getAdId() {
        return AdId;
    }

    public void setAdId(long adId) {
        AdId = adId;
    }

    public String getAdTitle() {
        return AdTitle;
    }

    public void setAdTitle(String adTitle) {
        AdTitle = adTitle;
    }

    public String getCollectionName() {
        return CollectionName;
    }

    public void setCollectionName(String collectionName) {
        CollectionName = collectionName;
    }

}
