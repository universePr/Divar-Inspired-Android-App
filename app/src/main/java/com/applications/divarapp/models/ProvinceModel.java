package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class ProvinceModel {
    @SerializedName("pid")
    private int Id;
    @SerializedName("name")
    private String Name;

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
}
