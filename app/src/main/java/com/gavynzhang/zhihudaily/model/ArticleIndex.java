package com.gavynzhang.zhihudaily.model;

/**
 * Created by GavynZhang on 2016/11/16 14:29.
 */

public class ArticleIndex {

    private String date;
    private String image_url;
    private String id;
    private String title;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
