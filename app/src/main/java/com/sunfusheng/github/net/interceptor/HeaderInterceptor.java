package com.sunfusheng.github.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class HeaderInterceptor implements Interceptor {

    private String authorization;

    public HeaderInterceptor(String authorization) {
        this.authorization = authorization;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request newRequest = oldRequest.newBuilder()
                .header("Authorization", authorization)
                .header("Accept", "application/vnd.github.v3.json")
                .method(oldRequest.method(), oldRequest.body())
                .build();

        return chain.proceed(newRequest);
    }
}
