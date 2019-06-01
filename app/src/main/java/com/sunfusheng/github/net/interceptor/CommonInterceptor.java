package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.response.ResponseData;
import com.sunfusheng.github.util.NetworkUtil;
import com.sunfusheng.github.util.PreferenceUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class CommonInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        @FetchMode int fetchMode = getFetchMode(request);
        Log.d("sfs", "BaseInterceptor url: " + request.url().toString() + " fetchMode: " + ResponseData.getFetchModeString(fetchMode));

        Request.Builder builder = request.newBuilder();
        String token = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "token " + token);
        }

        if (!NetworkUtil.isConnected() || fetchMode == FetchMode.LOCAL) {
            request = builder
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        } else {
            request = builder
                    .header("Cache-Control", getCacheControl(fetchMode))
                    .removeHeader("Pragma")
                    .build();
        }
        return chain.proceed(request);
    }

    public static int getFetchMode(Request request) {
        String fetchModeString = request.header("fetch_mode");
        if (fetchModeString == null) {
            return FetchMode.REMOTE;
        }

        @FetchMode int fetchMode;
        try {
            fetchMode = Integer.valueOf(fetchModeString);
        } catch (NumberFormatException e) {
            fetchMode = FetchMode.REMOTE;
        }

        if (fetchMode == FetchMode.DEFAULT) {
            fetchMode = FetchMode.REMOTE;
        }
        return fetchMode;
    }

    public static String getCacheControl(@FetchMode int fetchMode) {
        String cacheControl;
        if (NetworkUtil.isConnected()) {
            int maxAge = Integer.MAX_VALUE;
            if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
                maxAge = 0;
            }
            cacheControl = "public, max-age=" + maxAge;
        } else {
            cacheControl = "public, only-if-cached, max-stale=" + Integer.MAX_VALUE;
        }
        return cacheControl;
    }
}

