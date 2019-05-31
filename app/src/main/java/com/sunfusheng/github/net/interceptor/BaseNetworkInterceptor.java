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
    private int mFetchMode = FetchMode.DEFAULT;

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        mFetchMode = BaseInterceptor.getFetchMode(request, mFetchMode);
        Response response = chain.proceed(request);

        return response.newBuilder()
                .header("Cache-Control", BaseInterceptor.getCacheControl(mFetchMode))
                .removeHeader("Pragma")
                .build();
    }
}

