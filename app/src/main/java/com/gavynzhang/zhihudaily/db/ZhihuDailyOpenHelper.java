package com.gavynzhang.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GavynZhang on 2016/11/16 1:09.
 */

public class ZhihuDailyOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_ARTICLE_INDEX = "create table article_index(" +
            "date text," +
            //"aid integer primary key autoincrement, " +
            "image_url text, " +
            "id text primary key, " +
            "title text)";

    public static final String CREATE_ARTICLE_CONTENT = "create table article_content(" +
            //"aid integer primary key autoincrement," +
            "date text," +
            "body text, " +
            "image_source text, " + //图片的内容提供方
            "title text," +
            "image text," +     //在文章浏览界面中使用的大图
            "share_url text," +
            "thumbnail text," +     //栏目的缩略图
            "id text primary key," +
            "css text)";        //供手机端的 WebView(UIWebView) 使用

    public static final String CREATE_TOP_STORIES_INDEX = "create table top_stories_index(" +
            "date text," +
            "image_url text," +
            "id text primary key," +
            "title text)";

    public ZhihuDailyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE_INDEX);
        db.execSQL(CREATE_ARTICLE_CONTENT);
        db.execSQL(CREATE_TOP_STORIES_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
