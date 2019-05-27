package com.sunfusheng.github.cache.disklrucache.base;

import com.orhanobut.logger.Logger;
import com.sunfusheng.github.util.MD5Util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author by sunfusheng on 2019-05-26
 */
abstract public class BaseDiskLruCache {

    private DiskLruCache mDiskLruCache;

    abstract public File cacheDir();

    abstract public long maxSize();

    public int appVersion() {
        return 1;
    }

    public int valueCount() {
        return 1;
    }

    private void initDiskLruCache() {
        if (mDiskLruCache == null) {
            try {
                mDiskLruCache = DiskLruCache.open(cacheDir(), appVersion(), valueCount(), maxSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, String value) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            Logger.e("BaseDiskLruCache mDiskLruCache == null");
            return;
        }

        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(MD5Util.hashKey(key));
            OutputStream outputStream = editor.newOutputStream(0);
            outputStream.write(value.getBytes());
            outputStream.close();
            editor.commit();
            mDiskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            Logger.e("BaseDiskLruCache mDiskLruCache == null");
            return null;
        }

        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Util.hashKey(key));
            if (snapshot != null) {
                return snapshot.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean remove(String key) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            Logger.e("BaseDiskLruCache mDiskLruCache == null");
            return false;
        }

        try {
            return mDiskLruCache.remove(MD5Util.hashKey(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
