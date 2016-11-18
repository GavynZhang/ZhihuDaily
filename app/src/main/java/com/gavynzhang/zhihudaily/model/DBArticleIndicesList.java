package com.gavynzhang.zhihudaily.model;

import java.util.List;

/**
 * Created by GavynZhang on 2016/11/18 15:29.
 */

public class DBArticleIndicesList {
    private List<ArticleIndex> mArticleIndices;
    private Integer latestDate;

    public Integer getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(Integer latestDate) {
        this.latestDate = latestDate;
    }

    public List<ArticleIndex> getArticleIndices() {
        return mArticleIndices;
    }

    public void setArticleIndices(List<ArticleIndex> articleIndices) {
        mArticleIndices = articleIndices;
    }
}
