package com.sunfusheng.github.net.interceptor;

import com.sunfusheng.github.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class CommonHeaderInterceptor implements Interceptor {


    public CommonHeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder()
                .header("Accept", Constants.ACCEPT)
                .method(oldRequest.method(), oldRequest.body());

        return chain.proceed(builder.build());
    }
}
