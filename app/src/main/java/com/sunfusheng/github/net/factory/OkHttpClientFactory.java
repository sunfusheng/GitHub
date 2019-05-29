package com.sunfusheng.github.net.factory;

import com.sunfusheng.github.BuildConfig;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.interceptor.BaseInterceptor;
import com.sunfusheng.github.net.interceptor.BaseNetworkInterceptor;
import com.sunfusheng.github.util.CollectionUtil;

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
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new BaseInterceptor(fetchMode))
                .addNetworkInterceptor(new BaseNetworkInterceptor(fetchMode))
                .cache(new Cache(Constants.CacheDir.OKHTTP, MAX_CACHE_SIZE));

        if (BuildConfig.debugMode) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        }

        if (!CollectionUtil.isEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }
}
