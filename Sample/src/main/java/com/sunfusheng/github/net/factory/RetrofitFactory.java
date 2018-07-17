package com.sunfusheng.github.net.factory;

import com.sunfusheng.github.Constants;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class RetrofitFactory {

    public static Retrofit get(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit get(Interceptor... interceptors) {
        return get(OkHttpClientFactory.get(interceptors));
    }

    public static <T> T getService(Class<T> service, Interceptor... interceptors) {
        return get(interceptors).create(service);
    }
}
