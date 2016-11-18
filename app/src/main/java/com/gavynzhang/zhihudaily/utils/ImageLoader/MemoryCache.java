package com.gavynzhang.zhihudaily.utils.ImageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by GavynZhang on 2016/11/16 21:04.
 */

public class MemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCache() {
        InitMemoryCache();
    }

    private void InitMemoryCache(){
        final int maxMemory = (int)Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemoryCache.put(url,bmp);
    }
}
