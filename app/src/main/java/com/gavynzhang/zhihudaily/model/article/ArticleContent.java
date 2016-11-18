package com.gavynzhang.zhihudaily.model.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GavynZhang on 2016/11/16 13:02.
 */

public class ArticleContent {

    private String body;
    @SerializedName("image_source")
    private String imageSource;
    private String title;
    private String image;
    @SerializedName("share_url")
    private String shareUrl;
    private List<String> js;
    @SerializedName("ga_prefix")
    private String gaPrefix;
    private Section section;
    private List<String> images;
    private int type;
    private int id;
    private List<String> css;
    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return body;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
    public String getImageSource() {
        return imageSource;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
    public String getShareUrl() {
        return shareUrl;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }
    public List<String> getJs() {
        return js;
    }

    public void setGaPrefix(String gaPrefix) {
        this.gaPrefix = gaPrefix;
    }
    public String getGaPrefix() {
        return gaPrefix;
    }

    public void setSection(Section section) {
        this.section = section;
    }
    public Section getSection() {
        return section;
    }

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

    public void setCss(List<String> css) {
        this.css = css;
    }
    public List<String> getCss() {
        return css;
    }

}
