package com.sunfusheng.github.cache.disklrucache;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.cache.disklrucache.base.BaseDiskLruCache;

import java.io.File;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class ReadmeDiskLruCache extends BaseDiskLruCache {
    @Override
    public File cacheDir() {
        return Constants.CacheDir.README;
    }

    @Override
    public long maxSize() {
        return 1024 * 1024 * 20;
    }
}
