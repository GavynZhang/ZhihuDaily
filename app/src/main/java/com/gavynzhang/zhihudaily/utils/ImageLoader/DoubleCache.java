package com.gavynzhang.zhihudaily.utils.ImageLoader;

import android.graphics.Bitmap;

/**
 * Created by GavynZhang on 2016/11/16 22:19.
 */

public class DoubleCache implements ImageCache {
    ImageCache mMemoryCache = new MemoryCache();
    ImageCache mDiskCache = new DiskCache();

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if(bitmap == null){
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemoryCache.put(url,bmp);
        mDiskCache.put(url,bmp);
    }
}
