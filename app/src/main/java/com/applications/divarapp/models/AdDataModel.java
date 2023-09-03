package com.applications.divarapp.models;

import java.io.File;

public class AdDataModel {
    private File Image;

    public AdDataModel(File image) {
        Image = image;
    }

    public File getImage() {
        return Image;
    }

    public void setImage(File image) {
        Image = image;
    }
}
