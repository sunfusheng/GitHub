package com.sunfusheng.github.datasource;

import com.orhanobut.logger.Logger;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.cache.disklrucache.DiskLruCache;
import com.sunfusheng.github.util.MD5Util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author by sunfusheng on 2019-05-26
 */
public class ReadmeDataSource {

    private static ReadmeDataSource sInstant = new ReadmeDataSource();

    private ReadmeDataSource() {
    }

    public static ReadmeDataSource getInstant() {
        return sInstant;
    }

    private DiskLruCache mDiskLruCache;

    private void initDiskLruCache() {
        if (mDiskLruCache == null) {
            try {
                mDiskLruCache = DiskLruCache.open(Constants.CacheDir.README, 1, 1, 1024 * 1024 * 20);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cacheReadme(String repoFullName, String data) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            Logger.e("mDiskLruCache == null");
            return;
        }

        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(MD5Util.hashKey(repoFullName));
            OutputStream outputStream = editor.newOutputStream(0);
            outputStream.write(data.getBytes());
            outputStream.close();
            editor.commit();
            mDiskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getReadme(String repoFullName) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            Logger.e("mDiskLruCache == null");
            return null;
        }

        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Util.hashKey(repoFullName));
            if (snapshot != null) {
                return snapshot.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteReadme(String repoFullName) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            Logger.e("mDiskLruCache == null");
            return false;
        }

        try {
            return mDiskLruCache.remove(MD5Util.hashKey(repoFullName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
