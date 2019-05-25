package com.sunfusheng.github.util;

import android.os.Environment;

import java.io.File;

/**
 * @author sunfusheng on 2018/7/25.
 */
public class SdCardUtil {

    public static boolean isMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

    public static File getDiskCacheDir(String dirName) {
        String cachePath;
        if (isMounted()) {
            File cacheDirFile = AppUtil.getContext().getExternalCacheDir();
            if (cacheDirFile != null) {
                cachePath = cacheDirFile.getPath();
            } else {
                cachePath = AppUtil.getContext().getCacheDir().getPath();
            }
        } else {
            cachePath = AppUtil.getContext().getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + dirName);
    }

}
