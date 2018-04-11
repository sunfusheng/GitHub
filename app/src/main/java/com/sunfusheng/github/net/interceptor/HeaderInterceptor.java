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
public class HeaderInterceptor implements Interceptor {

    private boolean isLogin;
    private String auth;
    private String token;

    public HeaderInterceptor(boolean isLogin) {
        this.isLogin = isLogin;
        this.auth = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.AUTH);
        this.token = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .header("Accept", "application/vnd.github.v3.json");

//        if (isLogin) {
//            builder.header("Authorization", "Basic " + auth);
//        } else {
//            builder.header("Authorization", "token " + token);
//        }

        return chain.proceed(builder.build());
    }
}
