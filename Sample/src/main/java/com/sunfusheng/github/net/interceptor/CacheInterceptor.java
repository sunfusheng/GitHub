package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.util.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存拦截器
 *
 * @author by sunfusheng on 2018/4/8.
 */
public class CacheInterceptor implements Interceptor {

    private int fetchMode;

    public CacheInterceptor(@FetchMode int fetchMode) {
        this.fetchMode = fetchMode;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);
        boolean isSuccessful = originalResponse.isSuccessful() || originalResponse.code() == 304;
        Log.d("---------->", request.url() + " isSuccessful: " + isSuccessful + " code: " + originalResponse.code() + " fetchMode: " + fetchMode);
        if (!NetworkUtil.isConnected() || (isSuccessful && fetchMode == FetchMode.LOCAL)) {
            return originalResponse;
        }

        if (NetworkUtil.isConnected() && fetchMode != FetchMode.LOCAL) {
            int maxTime = 30 * 24 * 60 * 60; // 30天
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxTime)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxTime = 30 * 24 * 60 * 60; // 30天
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}

