package com.gavynzhang.zhihudaily.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gavynzhang.zhihudaily.R;
import com.gavynzhang.zhihudaily.app.MyApplication;
import com.gavynzhang.zhihudaily.config.API;
import com.gavynzhang.zhihudaily.db.ZhihuDailyDB;
import com.gavynzhang.zhihudaily.model.ArticleIndex;
import com.gavynzhang.zhihudaily.model.article.ArticleContent;
import com.gavynzhang.zhihudaily.model.before.Before;
import com.gavynzhang.zhihudaily.model.before.Stories;
import com.gavynzhang.zhihudaily.model.latest.Latest;
import com.gavynzhang.zhihudaily.utils.HttpCallBackListener;
import com.gavynzhang.zhihudaily.utils.HttpUtil;
import com.gavynzhang.zhihudaily.utils.LogUtils;
import com.gavynzhang.zhihudaily.utils.ParseJSON;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.ArrayList;
import java.util.List;

public class BeforeActivity extends AppCompatActivity {

    private String date = null;
    public static final int SHOW_RESPONSE = 0;
    private Before mBefore = null;
    private List<ArticleIndex> mArticleIndices = new ArrayList<>();
    private RecyclerView mRecyclerView;
    ZhihuDailyDB zhihuDailyDB = ZhihuDailyDB.getInstance(MyApplication.getContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before);

        mRecyclerView = (RecyclerView)findViewById(R.id.article_index_before);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        sendHttpRequest(API.getBEFORE(date));
    }

    public void sendHttpRequest(String address){
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(final String response) {
                Log.w("BeforeActivity", response);
                mBefore = ParseJSON.parseJsonToBefore(response);
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
                    LogUtils.w("BeforeActivity","This is in handler");
                    beforeToArticleIndex();
                    initViews();
            }
        }
    };

    private void beforeToArticleIndex(){
        LogUtils.w("BeforeActivity", mBefore.getDate());
//        if(date != mBefore.getDate()){
//            return;
//        }
        List<Stories> mBeforeStories = mBefore.getStories();
        for(int i = 0; i < mBeforeStories.size(); i++){
            ArticleIndex articleIndex = new ArticleIndex();
            articleIndex.setId(String.valueOf(mBeforeStories.get(i).getId()));
            articleIndex.setDate(date);
            articleIndex.setImage_url(mBeforeStories.get(i).getImages().get(0));
            articleIndex.setTitle(mBeforeStories.get(i).getTitle());
            mArticleIndices.add(articleIndex);
            //zhihuDailyDB.saveArticleIndex(articleIndex);
        }

    }

    private void initViews(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(BeforeActivity.this));
        mRecyclerView.setAdapter(new ArticleIndexAdapter(mArticleIndices));
    }

    class ArticleIndexAdapter extends RecyclerView.Adapter{

        List<ArticleIndex> mArticleIndices;

        ArticleIndexAdapter(List<ArticleIndex> articleIndices){
            this.mArticleIndices = articleIndices;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_index_item, null);
            ArticleIndexAdapter.ArticleIndexViewHolder viewHolder = new ArticleIndexAdapter.ArticleIndexViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ArticleIndexAdapter.ArticleIndexViewHolder vh = (ArticleIndexAdapter.ArticleIndexViewHolder)holder;

            final ArticleIndex articleIndexData = mArticleIndices.get(position);

            Glide.with(MyApplication.getContext()).load(articleIndexData.getImage_url()).into(vh.getArticleIndexImage());
            vh.getArticleIndexTitle().setText(articleIndexData.getTitle());

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArticleContentActivity.actionStart(BeforeActivity.this,articleIndexData.getId());
                    //Toast.makeText(MyApplication.getContext(), articleIndexData.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArticleIndices.size();
        }

        public class ArticleIndexViewHolder extends RecyclerView.ViewHolder{

            private TextView articleIndexTitle;
            private ImageView articleIndexImage;

            public ArticleIndexViewHolder(View itemView) {
                super(itemView);

                articleIndexTitle = (TextView)itemView.findViewById(R.id.article_index_title_text);
                articleIndexImage = (ImageView)itemView.findViewById(R.id.article_index_image);
            }

            public ImageView getArticleIndexImage() {
                return articleIndexImage;
            }

            public void setArticleIndexImage(ImageView articleIndexImage) {
                this.articleIndexImage = articleIndexImage;
            }

            public TextView getArticleIndexTitle() {
                return articleIndexTitle;
            }

            public void setArticleIndexTitle(TextView articleIndexTitle) {
                this.articleIndexTitle = articleIndexTitle;
            }
        }
    }
}
