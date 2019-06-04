package com.sunfusheng.github.net.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.db.AccessTimeDatabase;
import com.sunfusheng.github.model.AccessTime;
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
        @FetchMode int realFetchMode = getFetchMode(request);

        Request.Builder builder = request.newBuilder();
        builder.addHeader("real-fetch-mode", ResponseData.getFetchModeString(realFetchMode));
        String token = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "token " + token);
        }

        if (!NetworkUtil.isConnected() || realFetchMode == FetchMode.LOCAL) {
            request = builder
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        } else {
            request = builder
                    .header("Cache-Control", getCacheControl(realFetchMode))
                    .removeHeader("Pragma")
                    .build();
        }

        Response response = chain.proceed(request);
        saveAccessTime(request, response, realFetchMode);
        return response;
    }

    private void saveAccessTime(Request request, Response response, @FetchMode int fetchMode) {
        if (response.isSuccessful() && fetchMode != FetchMode.LOCAL) {
            AccessTimeDatabase.instance().getAccessTimeDao().insert(
                    new AccessTime(request.url().toString(), System.currentTimeMillis())
            );
        }
    }

    static int getFetchMode(Request request) {
        @FetchMode int fetchMode = FetchMode.REMOTE;
        String fetchModeString = request.header("fetch-mode");
        if (fetchModeString != null) {
            try {
                fetchMode = Integer.valueOf(fetchModeString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        AccessTime accessTime = AccessTimeDatabase.instance().getAccessTimeDao().query(request.url().toString());
        if (accessTime != null) {
            long localCacheValidateTime = getLocalCacheValidateTimeByRequestHeader(request);
            long betweenTime = (System.currentTimeMillis() - accessTime.lastAccessTime) / 1000;
            if (betweenTime < localCacheValidateTime && fetchMode != FetchMode.FORCE_REMOTE) {
                fetchMode = FetchMode.LOCAL;
            }
        }
        return fetchMode;
    }

    public static long getLocalCacheValidateTimeByRequestHeader(Request request) {
        long localCacheValidateTime = 0;
        String localCacheValidateTimeString = request.header("local-cache-validate-time");
        if (localCacheValidateTimeString != null) {
            try {
                localCacheValidateTime = Long.valueOf(localCacheValidateTimeString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return localCacheValidateTime;
    }

    static String getCacheControl(@FetchMode int fetchMode) {
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

