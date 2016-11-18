package com.gavynzhang.zhihudaily.model.latest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GavynZhang on 2016/11/16 12:49.
 */

public class TopStories {

    private String image;
    private int type;
    private int id;
    @SerializedName("ga_prefix")
    private String gaPrefix;
    private String title;
    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
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
