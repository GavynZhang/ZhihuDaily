package com.gavynzhang.zhihudaily.config;

/**
 * Created by GavynZhang on 2016/11/13 0:49.
 */

public class API {
    public static String START_IMAGE = "http://news-at.zhihu.com/api/4/start-image/1920*1080";
    public static String LATEST = "http://news-at.zhihu.com/api/4/news/latest";
    //主题日报列表查看
    public static String THEME = "http://news-at.zhihu.com/api/4/themes";

    /**
     * @param id 主题日报id
     * @return 组装好的URL
     */
    public static String getThemeContent(String id){
        return THEME+id;
    }

    public static String getNewsContent(String id){
        String NEWS_CONTENT = "http://news-at.zhihu.com/api/4/news/";
        return NEWS_CONTENT +id;
    }

    /**
     * @param date 格式需要为yyyymmdd
     * @return 组装好的URL
     */
    public static String getBEFORE(String date){
        String BEFORE = "http://news.at.zhihu.com/api/4/news/before/";
        return BEFORE +date;
    }

    /**
     * @param id 文章id
     * @return 组装好的URL
     */
    public static String getStoryExtra(String id){
        String STORY_EXTRA = "http://news-at.zhihu.com/api/4/story-extra/";
        return STORY_EXTRA +id;
    }

    public static String getLongComments(String id){
        return "http://news-at.zhihu.com/api/4/story/"+id+"/long-comments";
    }

    public static String getShortComments(String id){
        return "http://news-at.zhihu.com/api/4/story/"+id+"/short-comments";
    }

    /**
     * 获取新闻的推荐者
     *
     * @param id 内容id
     * @return URL
     */
    public static String getRecommenders(String id){
        return "http://news-at.zhihu.com/api/4/story/"+id+"/recommenders";
    }
}
