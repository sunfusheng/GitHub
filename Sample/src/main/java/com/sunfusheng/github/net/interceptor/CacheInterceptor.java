package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.util.NetworkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    private static CacheControl onlyIfCached = new CacheControl.Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    private static CacheControl.Builder cacheControlBuilder = new CacheControl.Builder();
    private int fetchMode;

    public CacheInterceptor(@FetchMode int fetchMode) {
        this.fetchMode = fetchMode;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isConnected() || fetchMode == FetchMode.LOCAL) {
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .cacheControl(onlyIfCached)
                    .build();
        } else {
            if (fetchMode == FetchMode.FORCE_REMOTE) {
                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .cacheControl(cacheControlBuilder
                                .maxAge(0, TimeUnit.SECONDS)
                                .build()
                        ).build();
            }
        }

        return chain.proceed(request);
    }
}
