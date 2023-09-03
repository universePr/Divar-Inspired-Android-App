package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class ChatModelResponse {

        @SerializedName("ad")
        private String AdTitle;
        @SerializedName("col")
        private String CollectionName;
        @SerializedName("phone")
        private String Phone;

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

        public String getPhone() {
                return Phone;
        }

        public void setPhone(String phone) {
                Phone = phone;
        }
}
