package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdBookMarkModelResponse {

    @SerializedName("isbookmark")
    private boolean IsBookMark;
    @SerializedName("isnote")
    private boolean NoteCheck;
    @SerializedName("note")
    private String Note;
    @SerializedName("aid")
    private long AdId;
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
    @SerializedName("hide")
    private boolean IsHide;
    @SerializedName("cmsg")
    private boolean CanMessage;
    @SerializedName("chatc")
    private String ChatCollectionName;
    @SerializedName("sph")
    private boolean ShowPhone;
    @SerializedName("areaid")
    private int AreaId;
    @SerializedName("cityid")
    private int CityId;
    @SerializedName("cityname")
    private String CityName;
    @SerializedName("cid")
    private int CategoryId;
    @SerializedName("catname")
    private String CategoryName;
    @SerializedName("sid")
    private int SubCategoryId;
    @SerializedName("scatname")
    private String SCategoryName;
    @SerializedName("ssid")
    private int SubSubCategoryId;
    @SerializedName("sscatname")
    private String SSCategoryName;
    @SerializedName("pid")
    private String Phone;
    @SerializedName("member")
    private String Membership;
    @SerializedName("time")
    private String DateTime;
    @SerializedName("imgs")
    private List<String> ImageUrls;

    public boolean isBookMark() {
        return IsBookMark;
    }

    public void setBookMark(boolean bookMark) {
        IsBookMark = bookMark;
    }

    public boolean isNoteCheck() {
        return NoteCheck;
    }

    public void setNoteCheck(boolean noteCheck) {
        NoteCheck = noteCheck;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public long getAdId() {
        return AdId;
    }

    public void setAdId(long adId) {
        AdId = adId;
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

    public boolean isHide() {
        return IsHide;
    }

    public void setHide(boolean hide) {
        IsHide = hide;
    }

    public boolean isCanMessage() {
        return CanMessage;
    }

    public void setCanMessage(boolean canMessage) {
        CanMessage = canMessage;
    }

    public String getChatCollectionName() {
        return ChatCollectionName;
    }

    public void setChatCollectionName(String chatCollectionName) {
        ChatCollectionName = chatCollectionName;
    }

    public boolean isShowPhone() {
        return ShowPhone;
    }

    public void setShowPhone(boolean showPhone) {
        ShowPhone = showPhone;
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

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSCategoryName() {
        return SCategoryName;
    }

    public void setSCategoryName(String SCategoryName) {
        this.SCategoryName = SCategoryName;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public void setSubSubCategoryId(int subSubCategoryId) {
        SubSubCategoryId = subSubCategoryId;
    }

    public String getSSCategoryName() {
        return SSCategoryName;
    }

    public void setSSCategoryName(String SSCategoryName) {
        this.SSCategoryName = SSCategoryName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMembership() {
        return Membership;
    }

    public void setMembership(String membership) {
        Membership = membership;
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
