package com.sunfusheng.github.net;

import com.sunfusheng.github.BuildConfig;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.net.interceptor.AuthHeaderInterceptor;
import com.sunfusheng.github.net.interceptor.CacheInterceptor;
import com.sunfusheng.github.net.interceptor.CommonHeaderInterceptor;
import com.sunfusheng.github.net.interceptor.LoginHeaderInterceptor;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.CollectionUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {

    private static ApiService apiCommonService;
    private static ApiService apiLoginService;
    private static ApiService apiAuthService;

    private Api() {
    }

    private static Retrofit getRetrofit(Interceptor... interceptors) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File cacheDir = new File(AppUtil.getApp().getCacheDir(), "HttpCache");
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 20);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache);

        if (BuildConfig.debugMode) {
            builder.addInterceptor(loggingInterceptor);
        }

        if (!CollectionUtil.isEmpty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        return new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static <T> T getService(Class<T> service, Interceptor... interceptors) {
        return getRetrofit(interceptors).create(service);
    }

    public static ApiService getCommonService() {
        if (apiCommonService == null) {
            apiCommonService = getService(ApiService.class, new CommonHeaderInterceptor());
        }
        return apiCommonService;
    }

    public static ApiService getLoginService() {
        if (apiLoginService == null) {
            apiLoginService = getService(ApiService.class, new LoginHeaderInterceptor());
        }
        return apiLoginService;
    }

    public static ApiService getAuthService() {
        if (apiAuthService == null) {
            apiAuthService = getService(ApiService.class, new AuthHeaderInterceptor());
        }
        return apiAuthService;
    }
}
