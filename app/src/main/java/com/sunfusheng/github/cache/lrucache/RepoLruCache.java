package com.sunfusheng.github.cache.lrucache;

import android.util.LruCache;

import com.sunfusheng.github.model.Repo;

/**
 * @author sunfusheng on 2018/7/24.
 */
public class RepoLruCache implements ILruCacheProvider<String, Repo> {
    private static final int MAX_CACHE_SIZE = 1000;
    private static final LruCache<String, Repo> cache = new LruCache<>(MAX_CACHE_SIZE);
    private static final RepoLruCache instance = new RepoLruCache();

    private RepoLruCache() {
    }

    public static RepoLruCache getInstance() {
        return instance;
    }

    @Override
    public Repo put(String key, Repo value) {
        return cache.put(key, value);
    }

    @Override
    public Repo get(String key) {
        return cache.get(key);
    }

    @Override
    public Repo remove(String key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        if (cache.size() > 0) {
            cache.evictAll();
        }
    }
}
