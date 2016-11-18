package com.gavynzhang.zhihudaily.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.gavynzhang.zhihudaily.model.DBArticleIndicesList;
import com.gavynzhang.zhihudaily.model.latest.Latest;
import com.gavynzhang.zhihudaily.model.latest.Stories;
import com.gavynzhang.zhihudaily.model.latest.TopStories;
import com.gavynzhang.zhihudaily.utils.ConstUtils;
import com.gavynzhang.zhihudaily.utils.HttpCallBackListener;
import com.gavynzhang.zhihudaily.utils.HttpUtil;
import com.gavynzhang.zhihudaily.utils.LogUtils;
import com.gavynzhang.zhihudaily.utils.ParseJSON;
import com.gavynzhang.zhihudaily.utils.TimeUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SHOW_RESPONSE = 0;
    public static final int SHOW_ROLLPAGERVIEW = 1;
    public static final int REQUEST_FAIL = 0;
    public static final int REQUEST_OK = 1;

    public Latest mLatest = null;

    private RecyclerView articleIndexListView;
    private RollPagerView mRollPagerView;

    private List<ArticleIndex> mArticleIndices = new ArrayList<ArticleIndex>();
    private List<ArticleIndex> mDBArticleIndices = new ArrayList<ArticleIndex>();
    private List<ArticleIndex> mNetworkArticleIndices = new ArrayList<>();
    private List<ArticleIndex> mTopStories = new ArrayList<>();
    private List<Bitmap> mTopStoriesBigImage = new ArrayList<>();
    private List<String> mTopStoriesId = new ArrayList<>();

    private String dbLatestDate = null;

    ZhihuDailyDB zhihuDailyDB = ZhihuDailyDB.getInstance(MyApplication.getContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleIndexListView = (RecyclerView)findViewById(R.id.article_index);
        mRollPagerView = (RollPagerView)findViewById(R.id.roll_page_view);

        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ArticleContentActivity.class);
                intent.putExtra("id",mTopStoriesId.get(position));
                startActivity(intent);
            }
        });

        //sendHttpRequest(API.LATEST);
        loadArticleIndexList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<String> beforeDate = getBeforeDate();
        final Menu menu = navigationView.getMenu();
        for (String date : beforeDate){
            menu.add(date)
                    .setIcon(R.drawable.tag)
                    .setTitle(date);
        }
    }



    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    latestToArticleIndex();
                    initRecyclerView(mArticleIndices);
                    getTopStoriesBitmaps();
                    //mRollPagerView.setAdapter(new rollViewPagerAdapter(getTopStoriesBitmapsUrl()));
                    break;
                case SHOW_ROLLPAGERVIEW:
                    LogUtils.w("MainActiity","设置RollPagerView的Adapter");
                    mRollPagerView.setAdapter(new rollViewPagerAdapter(mTopStoriesBigImage));
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = (String)item.getTitle();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
        //Toast.makeText(MainActivity.this,title,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, BeforeActivity.class);
        intent.putExtra("date", title);
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class rollViewPagerAdapter extends StaticPagerAdapter{

        List<Bitmap> mBitmaps = null;
        public rollViewPagerAdapter(List<Bitmap> bitmaps){
            LogUtils.w("MainActivity","Adapter构造方法");
            this.mBitmaps = bitmaps;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
                view.setImageBitmap(mBitmaps.get(position));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return mBitmaps.size();
        }
    }

    private void getTopStoriesBitmaps(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mLatest != null) {
                    for (int i = 0; i < mLatest.getTopStories().size(); i++) {
                        try {
                            LogUtils.w("MainActivity", "正在添加图片");
                            mTopStoriesBigImage.add(Glide.with(MainActivity.this).load(mLatest.getTopStories().get(i).getImage()).asBitmap().centerCrop().into(500, 500).get());
                            mTopStoriesId.add(String.valueOf(mLatest.getTopStories().get(i).getId()));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    LogUtils.w("MainActivity","从数据库加载topStories");
                    List<ArticleIndex> topStories = zhihuDailyDB.loadTopStoriesIndex(TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMdd")));

                    //如果今天的为空就加载昨天的
                    if (topStories.size() == 0){
                        LogUtils.w("MainActivity","今天的数据为空");
                        topStories = zhihuDailyDB.loadTopStoriesIndex(TimeUtils.milliseconds2String(TimeUtils.getCurTimeMills()- ConstUtils.DAY,new SimpleDateFormat("yyyyMMdd")));
                    }
                    if (topStories.size() != 0){
                        LogUtils.w("MainActivity", "正在添加图片");
                        for(int i = 0; i < topStories.size(); i++){
                            try {
                                mTopStoriesBigImage.add(Glide.with(MainActivity.this).load(topStories.get(i).getImage_url()).asBitmap().centerCrop().into(500,500).get());
                                mTopStoriesId.add(topStories.get(i).getId());
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                Message message = new Message();
                message.what = SHOW_ROLLPAGERVIEW;
                mHandler.sendMessage(message);
            }
        }).start();

    }

    private void initRecyclerView(List<ArticleIndex> articleIndices){
        articleIndexListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        articleIndexListView.setAdapter(new ArticleIndexAdapter(articleIndices));
    }

    public int[] sendHttpRequest(String address){
        final int[] isRequestOk = {0};
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            String resultDate = null;
            @Override
            public void onFinish(final String response) {
                Log.w("MainActivity", response);
                mLatest = ParseJSON.parseJsonToLatest(response);
                resultDate = mLatest.getDate();
                Log.w("MainActivityMLatest", resultDate);
                isRequestOk[0] = REQUEST_OK;
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                isRequestOk[0] = REQUEST_FAIL;
                e.printStackTrace();
            }
        });

        return isRequestOk;
    }

    /**
     * 转化Latest对象为ArticleIndex数组
     */
    public void latestToArticleIndex(){
        String date = mLatest.getDate();
        List<Stories> latestStories = mLatest.getStories();
        for(int i = 0; i < latestStories.size(); i++) {
            ArticleIndex articleIndex = new ArticleIndex();
            Stories story = latestStories.get(i);
            articleIndex.setDate(date);
            articleIndex.setImage_url(story.getImages().get(0));
            articleIndex.setTitle(story.getTitle());
            articleIndex.setId(String.valueOf(story.getId()));

            //添加到显示列表
            mNetworkArticleIndices.add(articleIndex);
            //添加到数据库
            zhihuDailyDB.saveArticleIndex(articleIndex);
        }
        combinedArticleIndexListDate();

        List<TopStories> topStories = mLatest.getTopStories();
        for(int i = 0; i < topStories.size(); i++){
            ArticleIndex articleIndex = new ArticleIndex();
            TopStories stories = topStories.get(i);
            articleIndex.setImage_url(stories.getImage());
            articleIndex.setTitle(stories.getTitle());
            articleIndex.setDate(date);
            articleIndex.setId(String.valueOf(stories.getId()));
            //存储Top_Stories到数据库
            zhihuDailyDB.saveTopStoriesIndex(articleIndex);
        }
    }

    public void loadArticleIndexList(){
//        int requestType = NO_NEED_REQUEST;
//
//        //Load today's data from databases.
//        List<ArticleIndex> dbArticleIndices = zhihuDailyDB.loadArticleIndex(TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMdd")));
//        //Load yesterday's data from databases.
//        List<ArticleIndex> dbArticleIndices_0 = zhihuDailyDB.loadArticleIndex(TimeUtils.milliseconds2String(TimeUtils.getCurTimeMills()- ConstUtils.DAY,new SimpleDateFormat("yyyyMMdd")));
//
//        if (dbArticleIndices.size() == 0 && dbArticleIndices_0.size() != 0){
//            requestType = HAVE_YESTERDAY_REQUEST;
//        }else if (dbArticleIndices.size() != 0 && dbArticleIndices_0.size() != 0){
//            requestType = NO_NEED_REQUEST;
//        }else if (dbArticleIndices.size() == 0 && dbArticleIndices_0.size() == 0){
//            requestType = NEED_REQUEST;
//        }
//
//        dbArticleIndices.addAll(dbArticleIndices_0);
//
//        if(dbArticleIndices.size() == 0){
//            LogUtils.w("MainActivity", "dbArticleIndices == null");
//            sendHttpRequest(API.LATEST);
//            //latestToArticleIndex();
//        }else if(isRequest == 1){
//            sendHttpRequest(API.LATEST);
//        } else{
//            LogUtils.w("MainActivity","dbArticleIndex is not empty  "+dbArticleIndices.get(0).getImage_url());
//            mArticleIndices = dbArticleIndices;
//            initRecyclerView(mArticleIndices);
//        }
//
//        Toast.makeText(MainActivity.this, TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMdd")), Toast.LENGTH_SHORT).show();
        dbLatestDate = loadArticleIndexFromDB();
        //int isRequestOk[] = {0};

        String nowDate = TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMdd"));
        if (dbLatestDate.equals(nowDate)){
            combinedArticleIndexListDate();
            initRecyclerView(mArticleIndices);
            getTopStoriesBitmaps();
        }else{
            combinedArticleIndexListDate();
            loadArticleIndexFromNetwork();
        }

//        if (isRequestOk[0] == REQUEST_OK){
//            if (mLatest.getDate().equals(dbLatestDate)) {
//                combinedArticleIndexListDate();
//                initRecyclerView(mArticleIndices);
//            }
//        }


    }

    public void combinedArticleIndexListDate(){
        if (mNetworkArticleIndices.size() != 0){
            mArticleIndices.addAll(mNetworkArticleIndices);
        }
        if (mDBArticleIndices.size() != 0) {
            mArticleIndices.addAll(mDBArticleIndices);
        }
    }

    public String loadArticleIndexFromDB(){
        DBArticleIndicesList dbArticleIndicesList = zhihuDailyDB.loadArticleIndex();
        mDBArticleIndices = dbArticleIndicesList.getArticleIndices();
        return String.valueOf(dbArticleIndicesList.getLatestDate());
    }

    public int[] loadArticleIndexFromNetwork(){
        int isRequestOk[] = sendHttpRequest(API.LATEST);
        return isRequestOk;
//        if (isRequestOk[0] == REQUEST_OK){
//            if (mLatest.getDate().equals(dbLatestDate)){
//
//            }
//        }
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
                    ArticleContentActivity.actionStart(MainActivity.this,articleIndexData.getId());
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

//    class BitmapAndId{
//        private Bitmap mBitmap;
//        private String id;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public Bitmap getBitmap() {
//            return mBitmap;
//        }
//
//        public void setBitmap(Bitmap bitmap) {
//            mBitmap = bitmap;
//        }
//    }
    private List<String> getBeforeDate(){
        List<String> beforeDate = new ArrayList<>();

        Integer mDate = Integer.valueOf(TimeUtils.milliseconds2String(TimeUtils.getCurTimeMills(),new SimpleDateFormat("yyyyMMdd")));
        for(int i = 1; i <= 30; i++){
            String date = TimeUtils.milliseconds2String(TimeUtils.getCurTimeMills() - i * ConstUtils.DAY,new SimpleDateFormat("yyyyMMdd"));
            if (mDate > Integer.valueOf(date)){
                mDate = Integer.valueOf(date);
                beforeDate.add(date);
            }

        }


        return beforeDate;
    }

}
