package com.gavynzhang.zhihudaily.model.latest;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by GavynZhang on 2016/11/16 1:39.
 */

public class Latest {
    private String date;
    private List<Stories> stories;
    @SerializedName("top_stories")
    private List<TopStories> topStories;
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }
    public List<Stories> getStories() {
        return stories;
    }

    public void setTopStories(List<TopStories> topStories) {
        this.topStories = topStories;
    }
    public List<TopStories> getTopStories() {
        return topStories;
    }

}
