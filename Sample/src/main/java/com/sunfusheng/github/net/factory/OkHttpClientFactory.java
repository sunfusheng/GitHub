package com.sunfusheng.github.net.factory;

import com.sunfusheng.github.BuildConfig;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.interceptor.CacheInterceptor;
import com.sunfusheng.github.util.CollectionUtil;
import com.sunfusheng.github.util.SdCardUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class OkHttpClientFactory {
    private static final int TIMEOUT = 60; // 60s
    private static final int MAX_CACHE_SIZE = 1024 * 1024 * 20; // 20MB

    public static OkHttpClient create(@FetchMode int fetchMode, Interceptor... interceptors) {
        Cache cache = new Cache(SdCardUtil.getDiskCacheDir("http-cache"), MAX_CACHE_SIZE);
        CacheInterceptor cacheInterceptor = new CacheInterceptor(fetchMode);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .cache(cache);

        if (BuildConfig.debugMode) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        if (!CollectionUtil.isEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }
}
