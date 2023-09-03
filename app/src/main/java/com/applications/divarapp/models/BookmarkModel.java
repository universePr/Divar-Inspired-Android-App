package com.applications.divarapp.models;

import com.google.gson.annotations.SerializedName;

public class BookmarkModel {
    @SerializedName("isbookmark")
    private boolean IsBookMark;
    @SerializedName("isnote")
    private boolean NoteCheck;
    @SerializedName("note")
    private String Note;

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
}
