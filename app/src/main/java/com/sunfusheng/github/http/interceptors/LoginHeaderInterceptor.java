package com.sunfusheng.github.http.interceptors;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.util.PreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class LoginHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                .header("Accept", Constants.ACCEPT_JSON)
                .header("Authorization", "Basic " + PreferenceUtil.getInstance().getString(Constants.PreferenceKey.AUTH))
                .build();

        return chain.proceed(request);
    }
}
