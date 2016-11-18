package com.gavynzhang.zhihudaily.utils;

import com.gavynzhang.zhihudaily.model.article.ArticleContent;
import com.gavynzhang.zhihudaily.model.before.Before;
import com.gavynzhang.zhihudaily.model.latest.Latest;
import com.google.gson.Gson;

/**
 * Created by GavynZhang on 2016/11/17 12:54.
 */

public class ParseJSON {
    public static Latest parseJsonToLatest(String jsonData){
        Gson gson = new Gson();
        Latest latest = gson.fromJson(jsonData, Latest.class);
        return latest;
    }
    public static Before parseJsonToBefore(String jsonData){
        Gson gson = new Gson();
        Before before = gson.fromJson(jsonData, Before.class);
        return before;
    }
    public static ArticleContent parseJsonToArticleContent(String jsonData){
        Gson gson = new Gson();
        ArticleContent articleContent = gson.fromJson(jsonData, ArticleContent.class);
        return articleContent;
    }

}
