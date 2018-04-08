package com.sunfusheng.github.net;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.net.interceptor.CacheInterceptor;
import com.sunfusheng.github.net.interceptor.HeaderInterceptor;
import com.sunfusheng.github.net.interceptor.LogInterceptor;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.PreferenceUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {

    private static ApiService apiService;

    private Api() {
        apiService = getRetrofit().create(ApiService.class);
    }

    private static Retrofit getRetrofit() {
        File cacheDir = new File(AppUtil.getApp().getCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 20);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
                .addInterceptor(new HeaderInterceptor(PreferenceUtil.getInstance().getString(Constants.PreferenceKey.AUTH)))
                .addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache);

        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static <T> T getService(final Class<T> service) {
        return getRetrofit().create(service);
    }

    public static ApiService getService() {
        if (apiService == null) {
            apiService = getService(ApiService.class);
        }
        return apiService;
    }

}
