package com.gavynzhang.zhihudaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gavynzhang.zhihudaily.R;
import com.gavynzhang.zhihudaily.app.MyApplication;
import com.gavynzhang.zhihudaily.config.API;
import com.gavynzhang.zhihudaily.db.ZhihuDailyDB;
import com.gavynzhang.zhihudaily.model.article.ArticleContent;
import com.gavynzhang.zhihudaily.utils.HttpCallBackListener;
import com.gavynzhang.zhihudaily.utils.HttpUtil;
import com.gavynzhang.zhihudaily.utils.LogUtils;
import com.gavynzhang.zhihudaily.utils.ParseJSON;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;
import com.zzhoujay.richtext.callback.OnImageClickListener;

import java.util.List;

public class ArticleContentActivity extends AppCompatActivity {

    public static final int SHOW_RESPONSE = 0;

    private ArticleContent mArticleContent = null;

    private ImageView articleContentBigImage;
    private TextView articleContentText;
    String articleId = null;

    ZhihuDailyDB zhihuDailyDB = ZhihuDailyDB.getInstance(MyApplication.getContext());

    public static void actionStart(Context context, String id){
        Intent intent = new Intent(context, ArticleContentActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        Intent intent = getIntent();
        articleId = intent.getStringExtra("id");
        LogUtils.w("ArticleContentActivity", articleId);

        articleContentBigImage = (ImageView)findViewById(R.id.article_content_big_image);
        articleContentText = (TextView)findViewById(R.id.article_content_text);

        sendHttpRequest(API.getNewsContent(articleId));
    }

    public void sendHttpRequest(String address){
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            String resultDate = null;
            @Override
            public void onFinish(final String response) {
                Log.w("ArticleContentActivity", response);
                mArticleContent = ParseJSON.parseJsonToArticleContent(response);
                //zhihuDailyDB.saveArticleContent(mArticleContent);
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                //loadArticleFromDB(articleId);
//                if (mArticleContent != null){
//                    Message message= new Message();
//                    message.what = SHOW_RESPONSE;
//                    mHandler.sendMessage(message);
//                }
            }
        });

    }



    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:

                    Glide.with(ArticleContentActivity.this).load(mArticleContent.getImage()).into(articleContentBigImage);
                    //articleContentText.setText(mArticleContent.getBody());
                    RichText.fromHtml(mArticleContent.getBody())
                            .autoFix(false)
                            .fix(new ImageFixCallback() {
                                @Override
                                public void onFix(ImageHolder holder) {
                                    //holder.setAutoFix(true);
                                    holder.setScaleType(ImageHolder.ScaleType.DEFAULT);
                                }
                            })
                            .into(articleContentText);
            }
        }
    };

    private void loadArticleFromDB(String id){
        mArticleContent = zhihuDailyDB.loadArticleContent(id);
    }


}
