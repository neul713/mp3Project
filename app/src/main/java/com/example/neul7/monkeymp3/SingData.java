package com.example.neul7.monkeymp3;

import android.graphics.Bitmap;

public class SingData {
    String id;
    String imageView;
    String txtTitle;
    String txtSinger;
    String txtTime;

    public SingData(String imageView, String txtTitle, String txtSinger, String txtTime) {
        this.imageView = imageView;
        this.txtTitle = txtTitle;
        this.txtSinger = txtSinger;
        this.txtTime = txtTime;
    }

    public SingData(String id, String imageView, String txtTitle, String txtSinger, String txtTime) {
        this.id = id;
        this.imageView = imageView;
        this.txtTitle = txtTitle;
        this.txtSinger = txtSinger;
        this.txtTime = txtTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public String getTxtSinger() {
        return txtSinger;
    }

    public void setTxtSinger(String txtSinger) {
        this.txtSinger = txtSinger;
    }

    public String getTxtTime() {
        return txtTime;
    }

    public void setTxtTime(String txtTime) {
        this.txtTime = txtTime;
    }

}
