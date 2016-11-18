package com.gavynzhang.zhihudaily.utils.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by GavynZhang on 2016/11/16 21:00.
 */

public class ImageLoader {
    ImageCache mImageCache = new MemoryCache();
    //线程池，线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //注入缓存实现
    public void setImageCache(ImageCache cache){
        mImageCache = cache;
    }

    public void displayImage(String imageUrl, ImageView imageView){
        Bitmap bitmap = mImageCache.get(imageUrl);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        submitLoadRequest(imageUrl, imageView);
    }

    public void submitLoadRequest(final String imageUrl, final ImageView imageView){
        imageView.setTag(imageUrl);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(imageUrl);
                if(bitmap == null){
                    return;
                }
                if(imageView.getTag().equals(imageUrl)){
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(imageUrl, bitmap);
            }
        });
    }

    public Bitmap downloadImage(String imageUrl){
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
