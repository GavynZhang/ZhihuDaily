package com.gavynzhang.zhihudaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gavynzhang.zhihudaily.model.ArticleIndex;
import com.gavynzhang.zhihudaily.model.DBArticleIndicesList;
import com.gavynzhang.zhihudaily.model.article.ArticleContent;
import com.gavynzhang.zhihudaily.model.article.Section;
import com.gavynzhang.zhihudaily.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GavynZhang on 2016/11/16 14:06.
 */

public class ZhihuDailyDB {

    public static final String DB_NAME = "zhihu_daily.db";
    public static final int VERSION = 1;
    public static ZhihuDailyDB zhihuDailyDB = null;
    private SQLiteDatabase db = null;

    private ZhihuDailyDB(Context context){
        ZhihuDailyOpenHelper dbHelper = new ZhihuDailyOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static ZhihuDailyDB getInstance(Context context){
        if(zhihuDailyDB == null){
            zhihuDailyDB = new ZhihuDailyDB(context);
        }
        return zhihuDailyDB;
    }

    public void saveArticleIndex(ArticleIndex articleIndex){
        if(articleIndex != null){
            LogUtils.d("ZhihuDailyDB","articleIndex != null");
            ContentValues values = new ContentValues();
            values.put("date", articleIndex.getDate());
            values.put("image_url", articleIndex.getImage_url());
            values.put("id", articleIndex.getId());
            values.put("title",articleIndex.getTitle());
            db.insert("article_index", null, values);
        }
    }

    public void saveArticleContent(ArticleContent articleContent){
        if(articleContent != null){
            ContentValues values = new ContentValues();
            values.put("body",articleContent.getBody());
            values.put("image_source",articleContent.getImageSource());
            values.put("title",articleContent.getTitle());
            values.put("image",articleContent.getImage());
            values.put("share_url",articleContent.getShareUrl());
            values.put("thumbnail",articleContent.getSection().getThumbnail());
            values.put("id",articleContent.getId());
            values.put("css",articleContent.getCss().get(0));
            db.insert("article_content",null,values);
        }
    }

    public List<ArticleIndex> loadArticleIndex(String date){
        List<ArticleIndex> list = new ArrayList<ArticleIndex>();
        Cursor cursor = db.query("article_index",null,"date="+date,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                ArticleIndex articleIndex = new ArticleIndex();
                articleIndex.setId(cursor.getString(cursor.getColumnIndex("id")));
                articleIndex.setDate(cursor.getString(cursor.getColumnIndex("date")));
                articleIndex.setImage_url(cursor.getString(cursor.getColumnIndex("image_url")));
                articleIndex.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                list.add(articleIndex);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public DBArticleIndicesList loadArticleIndex(){

        DBArticleIndicesList dbArticleIndicesList = new DBArticleIndicesList();
        List<ArticleIndex> list = new ArrayList<ArticleIndex>();
        Integer latestDate = 0;

        Cursor cursor = db.query("article_index",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                ArticleIndex articleIndex = new ArticleIndex();
                String date;
                articleIndex.setId(cursor.getString(cursor.getColumnIndex("id")));
                date = cursor.getString(cursor.getColumnIndex("date"));
                articleIndex.setDate(date);
                if (latestDate < Integer.parseInt(date))
                    latestDate = Integer.parseInt(date);
                articleIndex.setImage_url(cursor.getString(cursor.getColumnIndex("image_url")));
                articleIndex.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                list.add(articleIndex);
            }while (cursor.moveToNext());
        }
        cursor.close();

        dbArticleIndicesList.setArticleIndices(list);
        dbArticleIndicesList.setLatestDate(latestDate);
        return dbArticleIndicesList;
    }

    public ArticleContent loadArticleContent(String id){
        ArticleContent articleContent = new ArticleContent();
        Cursor cursor = db.query("article_content",null,"id="+id,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                articleContent.setBody(cursor.getString(cursor.getColumnIndex("body")));
                articleContent.setImageSource(cursor.getString(cursor.getColumnIndex("image_source")));
                articleContent.setImage(cursor.getString(cursor.getColumnIndex("image")));
                articleContent.setId(cursor.getInt(cursor.getColumnIndex("id")));
                articleContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                articleContent.setShareUrl(cursor.getString(cursor.getColumnIndex("share_url")));

                String css = cursor.getString(cursor.getColumnIndex("css"));
                List<String> cssStrings = new ArrayList<String>();
                cssStrings.add(css);
                articleContent.setCss(cssStrings);

                Section section = new Section();
                section.setThumbnail(cursor.getString(cursor.getColumnIndex("thumbnail")));
                //section.setId(cursor.getInt(cursor.getColumnIndex("id")));
                //section.setName(cursor.getString(cursor.getColumnIndex("n")));
                articleContent.setSection(section);

            }while(cursor.moveToNext());
        }
        cursor.close();
        return articleContent;
    }

    public List<ArticleIndex> loadTopStoriesIndex(String date){
        List<ArticleIndex> topStoriesIndices = new ArrayList<>();
        Cursor cursor = db.query("top_stories_index",null,"date="+date,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                ArticleIndex articleIndex = new ArticleIndex();
                articleIndex.setId(cursor.getString(cursor.getColumnIndex("id")));
                articleIndex.setImage_url(cursor.getString(cursor.getColumnIndex("image_url")));
                articleIndex.setDate(cursor.getString(cursor.getColumnIndex("date")));
                articleIndex.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                topStoriesIndices.add(articleIndex);
            }while (cursor.moveToNext());
        }
        return topStoriesIndices;
    }

    public void saveTopStoriesIndex(ArticleIndex articleIndex){
        if(articleIndex != null){
            LogUtils.d("ZhihuDailyDB","articleIndex != null");
            ContentValues values = new ContentValues();
            values.put("date", articleIndex.getDate());
            values.put("image_url", articleIndex.getImage_url());
            values.put("id", articleIndex.getId());
            values.put("title",articleIndex.getTitle());
            db.insert("top_stories_index", null, values);
        }
    }



}
