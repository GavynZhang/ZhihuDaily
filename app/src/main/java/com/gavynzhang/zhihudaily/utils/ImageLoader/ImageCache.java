package com.gavynzhang.zhihudaily.utils.ImageLoader;

import android.graphics.Bitmap;

/**
 * Created by GavynZhang on 2016/11/16 21:01.
 */

public interface ImageCache {
    Bitmap get(String url);
    void put(String url, Bitmap bmp);
}
