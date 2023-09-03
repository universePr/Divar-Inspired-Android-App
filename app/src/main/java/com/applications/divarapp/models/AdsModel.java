package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AdsModel implements Serializable {
    @SerializedName("aid")
    private long AdId ;
    @SerializedName("title")
    private String Title;
    @SerializedName("desc")
    private String Description;
    @SerializedName("price")
    private double FinalPrice;
    @SerializedName("status")
    private boolean Status;
    @SerializedName("img")
    private boolean ContainImage;
    @SerializedName("cmsg")
    private boolean CanMessage;
    @SerializedName("sph")
    private boolean ShowPhon;
    @SerializedName("areaid")
    private int AreaId;
    @SerializedName("catname")
    public String CategoryName;
    @SerializedName("cityid")
    private int CityId;
    @SerializedName("cityname")
    private String CityName;
    @SerializedName("cid")
    private int CategoryId;
    @SerializedName("sid")
    private int SubCategoryId;
    @SerializedName("ssid")
    private int SubSubCategoryId;
    @SerializedName("pid")
    private String Phone;
    @SerializedName("time")
    private String DateTime;
    @SerializedName("hide")
    private boolean IsHide;
    @SerializedName("imgs")
    private List<String> ImageUrls;
    @SerializedName("member")
    private String Membership;
    @SerializedName("scatname")
    public String SCategoryName;
    @SerializedName("sscatname")
    public String SSCategoryName;
    @SerializedName("chatc")
    private String ChatCollectionName;

    public String getChatCollectionName() {
        return ChatCollectionName;
    }

    public void setChatCollectionName(String chatCollectionName) {
        ChatCollectionName = chatCollectionName;
    }

    public String getSCategoryName() {
        return SCategoryName;
    }

    public void setSCategoryName(String SCategoryName) {
        this.SCategoryName = SCategoryName;
    }

    public String getSSCategoryName() {
        return SSCategoryName;
    }

    public void setSSCategoryName(String SSCategoryName) {
        this.SSCategoryName = SSCategoryName;
    }

    public String getMembership() {
        return Membership;
    }

    public void setMembership(String membership) {
        Membership = membership;
    }

    public boolean isHide() {
        return IsHide;
    }

    public void setHide(boolean hide) {
        IsHide = hide;
    }

    public long getAdId() {
        return AdId;
    }

    public void setAdId(long adId) {
        AdId = adId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getFinalPrice() {
        return FinalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        FinalPrice = finalPrice;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public boolean isContainImage() {
        return ContainImage;
    }

    public void setContainImage(boolean containImage) {
        ContainImage = containImage;
    }

    public boolean isCanMessage() {
        return CanMessage;
    }

    public void setCanMessage(boolean canMessage) {
        CanMessage = canMessage;
    }

    public boolean isShowPhon() {
        return ShowPhon;
    }

    public void setShowPhon(boolean showPhon) {
        ShowPhon = showPhon;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        SubSubCategoryId = subSubCategoryId;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public List<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        ImageUrls = imageUrls;
    }
}
