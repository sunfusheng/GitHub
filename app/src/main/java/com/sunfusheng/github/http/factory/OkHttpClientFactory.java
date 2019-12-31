package com.sunfusheng.github.http.factory;

import com.sunfusheng.github.BuildConfig;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.http.interceptors.CommonInterceptor;
import com.sunfusheng.github.http.interceptors.CommonNetworkInterceptor;
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
    private static final int TIMEOUT = 30; // 30s
    private static final int MAX_CACHE_SIZE = 1024 * 1024 * 20; // 20MB

    public static OkHttpClient create(Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new CommonInterceptor())
                .addNetworkInterceptor(new CommonNetworkInterceptor())
                .cache(new Cache(Constants.CacheDir.OKHTTP, MAX_CACHE_SIZE));

        if (BuildConfig.debugMode) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        if (!CollectionUtil.isEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }
}
