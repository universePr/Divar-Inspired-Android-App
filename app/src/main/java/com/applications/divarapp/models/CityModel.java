package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class CityModel {
    @SerializedName("cid")
    private int Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("pid")
    private int ProvinceId ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int provinceId) {
        ProvinceId = provinceId;
    }
}
