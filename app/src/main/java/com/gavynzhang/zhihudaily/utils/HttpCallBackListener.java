package com.gavynzhang.zhihudaily.utils;

/**
 * Created by GavynZhang on 2016/11/16 17:09.
 */

public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
