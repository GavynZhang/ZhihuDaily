package com.gavynzhang.zhihudaily.model.article;

/**
 * Created by GavynZhang on 2016/11/16 13:03.
 */

public class Section {

    private String thumbnail;
    private int id;
    private String name;
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
