package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class BaseNetworkInterceptor implements Interceptor {

    @FetchMode
    private int fetchMode;

    public BaseNetworkInterceptor(@FetchMode int fetchMode) {
        this.fetchMode = fetchMode;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        return response.newBuilder()
                .header("Cache-Control", BaseInterceptor.getCacheControl(fetchMode))
                .removeHeader("Pragma")
                .build();
    }
}

