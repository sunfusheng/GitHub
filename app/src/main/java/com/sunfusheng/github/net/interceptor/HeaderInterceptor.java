package com.sunfusheng.github.net.interceptor;

import android.text.TextUtils;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.util.PreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class HeaderInterceptor implements Interceptor {

    private String auth;
    private String token;

    public HeaderInterceptor() {
        this.auth = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.AUTH);
        this.token = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .header("Accept", "application/vnd.github.v3.json");

        if (!TextUtils.isEmpty(token)) {
            builder.header("Authorization", "token " + token);
        } else if (!TextUtils.isEmpty(auth)) {
            builder.header("Authorization", "Basic " + auth);
        }
        return chain.proceed(builder.build());
    }
}
