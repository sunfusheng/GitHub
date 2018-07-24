package com.sunfusheng.github.lrucache;

import android.util.LruCache;

import com.sunfusheng.github.model.Repo;

/**
 * @author sunfusheng on 2018/7/24.
 */
public class RepoLruCache implements ILruCacheProvider<Integer, Repo> {
    private static final int MAX_CACHE_SIZE = 1000;
    private static final LruCache<Integer, Repo> cache = new LruCache<>(MAX_CACHE_SIZE);
    private static final RepoLruCache instance = new RepoLruCache();

    private RepoLruCache() {
    }

    public static RepoLruCache getInstance() {
        return instance;
    }

    @Override
    public Repo put(Integer key, Repo value) {
        return cache.put(key, value);
    }

    @Override
    public Repo get(Integer key) {
        return cache.get(key);
    }

    @Override
    public Repo remove(Integer key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        if (cache.size() > 0) {
            cache.evictAll();
        }
    }
}
