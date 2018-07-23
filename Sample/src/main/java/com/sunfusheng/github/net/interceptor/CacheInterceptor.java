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
    private static CacheControl.Builder maxStaleCacheControlBuilder = new CacheControl.Builder().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS);
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
            if (request.cacheControl() != null) {
                int maxAge = request.cacheControl().maxAgeSeconds();
                if (maxAge < 0 || fetchMode == FetchMode.REMOTE) {
                    maxAge = 0;
                }

                int maxStale = Integer.MAX_VALUE;
                if (fetchMode == FetchMode.REMOTE) {
                    maxStale = 0;
                }
                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .cacheControl(maxStaleCacheControlBuilder
                                .maxAge(maxAge, TimeUnit.SECONDS)
                                .maxStale(maxStale, TimeUnit.SECONDS)
                                .build()
                        ).build();
            }
        }

        Response response = chain.proceed(request);
        return response.newBuilder().build();
    }
}
