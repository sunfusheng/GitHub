package com.sunfusheng.github.net.interceptor;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.util.PreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class AuthHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder()
                .header("Accept", Constants.ACCEPT)
                .header("Authorization", "token " + PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN))
                .method(oldRequest.method(), oldRequest.body());

        return chain.proceed(builder.build());
    }
}
