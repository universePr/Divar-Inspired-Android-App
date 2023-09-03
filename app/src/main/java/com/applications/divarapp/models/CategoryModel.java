package com.applications.divarapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryModel {
    @SerializedName("c_id")
    private int Id ;
    @SerializedName("c_name")
    private String Name ;
    @SerializedName("c_icon")
    private String IconUrl ;
    @SerializedName("c_subs")
    private ArrayList<SubCategoryModel> subs ;

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

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String iconUrl) {
        IconUrl = iconUrl;
    }

    public ArrayList<SubCategoryModel> getSubs() {
        return subs;
    }

    public void setSubs(ArrayList<SubCategoryModel> subs) {
        this.subs = subs;
    }

    public static class SubCategoryModel implements Parcelable {
        @SerializedName("s_id")
        private int Id;
        @SerializedName("s_name")
        private String Name;
        @SerializedName("s_subs")
        private final ArrayList<SubCategoryModel> subs;

        protected SubCategoryModel(Parcel in) {
            Id = in.readInt();
            Name = in.readString();
            subs = in.createTypedArrayList(CREATOR);
        }

        public static final Creator<SubCategoryModel> CREATOR = new Creator<SubCategoryModel>() {
            @Override
            public SubCategoryModel createFromParcel(Parcel in) {
                return new SubCategoryModel(in);
            }

            @Override
            public SubCategoryModel[] newArray(int size) {
                return new SubCategoryModel[size];
            }
        };

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

        public ArrayList<SubCategoryModel> getSubs() {
            return subs;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeInt(Id);
            dest.writeString(Name);
            dest.writeTypedList(subs);
        }
    }
}
