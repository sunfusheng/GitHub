package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;
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
public class BaseInterceptor implements Interceptor {

    @FetchMode
    private int mFetchMode = FetchMode.DEFAULT;

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        mFetchMode = getFetchMode(request, mFetchMode);
        Log.d("sfs", "fetchMode: " + ResponseData.getFetchModeString(mFetchMode));

        Request.Builder requestBuilder = request.newBuilder().addHeader("Authorization", "token " + PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN));
        if (!NetworkUtil.isConnected() || mFetchMode == FetchMode.LOCAL) {
            request = requestBuilder
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        } else {
            request = requestBuilder.build();
        }

        Response response = chain.proceed(request);
        return response.newBuilder()
                .header("Cache-Control", getCacheControl(mFetchMode))
                .removeHeader("Pragma")
                .build();
    }

    public static int getFetchMode(Request request, @FetchMode int lastFetchMode) {
        String fetchModeString = request.header("fetch_mode");
        Log.d("sfs", "getFetchMode(): " + fetchModeString);
        if (fetchModeString != null) {
            return Integer.valueOf(fetchModeString);
        }

        if (lastFetchMode != FetchMode.DEFAULT) {
            return lastFetchMode;
        }
        return FetchMode.REMOTE;
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

