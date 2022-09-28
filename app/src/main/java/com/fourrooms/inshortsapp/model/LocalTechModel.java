package com.fourrooms.inshortsapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LocalTechModel implements Serializable {
    @SerializedName("category")
    public String category;

    @SerializedName("imgUrl")
    public String imgUrl;

    @SerializedName("title")
    public String title;

    public String thumn;

    @SerializedName("ids")
    @NonNull
    private int ids;

    public LocalTechModel(String category) {
        this.category = category;
    }

    public LocalTechModel(String category, String imgUrl, String title, String thumn, int ids) {
        this.category = category;
        this.imgUrl = imgUrl;
        this.title = title;
        this.thumn = thumn;
        this.ids = ids;
    }

    public String getThumn() {
        return thumn;
    }

    public void setThumn(String thumn) {
        this.thumn = thumn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }
}
