package com.gavynzhang.zhihudaily.model.before;

import java.util.List;

/**
 * Created by GavynZhang on 2016/11/16 12:54.
 */

public class Before {

    private String date;
    private List<Stories> stories;
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

}
