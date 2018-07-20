package com.sunfusheng.github.net.factory;

import com.sunfusheng.github.BuildConfig;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.interceptor.CacheInterceptor;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.CollectionUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class OkHttpClientFactory {

    public static OkHttpClient get(@FetchMode int fetchMode, Interceptor... interceptors) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File cacheDir = new File(AppUtil.getApp().getCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 20);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new CacheInterceptor(fetchMode))
                .addNetworkInterceptor(new CacheInterceptor(fetchMode))
                .cache(cache);

        if (BuildConfig.debugMode) {
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
