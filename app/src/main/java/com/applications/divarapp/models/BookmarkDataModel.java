package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class BookmarkDataModel {

    @SerializedName("isbookmark")
    private boolean IsBookMark;
    @SerializedName("isnote")
    private boolean NoteCheck;
    @SerializedName("note")
    private String Note;
    @SerializedName("uph")
    private String UserPhone;
    @SerializedName("aid")
    private long AdId;

    public BookmarkDataModel(boolean isBookMark, boolean noteCheck, String note, String userPhone, long adId) {
        IsBookMark = isBookMark;
        NoteCheck = noteCheck;
        Note = note;
        UserPhone = userPhone;
        AdId = adId;
    }

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

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public long getAdId() {
        return AdId;
    }

    public void setAdId(long adId) {
        AdId = adId;
    }
}
