package com.gavynzhang.zhihudaily.model.latest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GavynZhang on 2016/11/16 12:37.
 */

public class Stories {

    private List<String> images;
    private int type;
    private int id;
    @SerializedName("ga_prefix")
    private String gaPrefix;
    private String title;
    public void setImages(List<String> images) {
        this.images = images;
    }
    public List<String> getImages() {
        return images;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setGaPrefix(String gaPrefix) {
        this.gaPrefix = gaPrefix;
    }
    public String getGaPrefix() {
        return gaPrefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

}
