package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class ChatResponse {
    @SerializedName("res")
    private boolean response;

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
