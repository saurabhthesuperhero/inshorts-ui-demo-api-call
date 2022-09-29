package com.fourrooms.inshortsapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "sample_table")
public class LocalTechModelRoom implements Serializable {
    @SerializedName("category")
    public String category;
    @ColumnInfo(name = "imgUrl")

    @SerializedName("imgUrl")
    public String imgUrl;

    @ColumnInfo(name = "title")

    @SerializedName("title")
    public String title;

    public String thumn;

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ids")
    @NonNull
    private int ids;

    public LocalTechModelRoom(String category) {
        this.category = category;
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
