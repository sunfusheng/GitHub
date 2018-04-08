package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;

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

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isConnected()) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (NetworkUtil.isConnected()) {
            String cacheControl = request.cacheControl().toString();
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", cacheControl)
                    .build();
        } else {
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", CacheControl.FORCE_CACHE.toString())
                    .build();
        }
    }
}
