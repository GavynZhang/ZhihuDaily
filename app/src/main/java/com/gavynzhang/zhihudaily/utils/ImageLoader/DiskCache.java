package com.gavynzhang.zhihudaily.utils.ImageLoader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.gavynzhang.zhihudaily.app.MyApplication;
import com.gavynzhang.zhihudaily.utils.libcore.io.DiskLruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by GavynZhang on 2016/11/16 21:14.
 */

public class DiskCache implements ImageCache {

//    DiskLruCache mDiskLruCache = null;

//    public DiskCache(){
//        InitDiskCache(MyApplication.getContext());
//    }

    static File mFile = getDiskCacheDir(MyApplication.getContext(),"bitmap");
    private static String cacheDir = mFile.getPath();

    @Override
    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(cacheDir+url);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir+url);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    /**
//     * 初始化DiskLruCache
//     *
//     * @param context
//     */
//    private void InitDiskCache(Context context){
//        try {
//            File cacheDir = getDiskCacheDir(context, "bitmap");
//            if (!cacheDir.exists()) {
//                cacheDir.mkdirs();
//            }
//            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public int getAppVersion(Context context) {
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            return info.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return 1;
//    }


    /**
     * 获取路径
     *
     * @param context
     * @param uniqueName：为了对不同类型的数据进行区分而设定的一个唯一值，比如说在网易新闻缓存路径下看到的bitmap、object等文件夹
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 通过URL计算key用于DiskLruCache存储
     *
     * @param key：待计算的string
     * @return 计算后的MD5值
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
