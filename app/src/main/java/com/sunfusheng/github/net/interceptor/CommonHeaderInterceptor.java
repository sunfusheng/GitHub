package com.sunfusheng.github.net.interceptor;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.util.PreferenceUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class CommonHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body());

        HashMap<String, String> map = getCommonHeaders();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.header(entry.getKey(), entry.getValue());
        }
        return chain.proceed(builder.build());
    }

    public static HashMap getCommonHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", Constants.USER_AGENT);
        headers.put("Accept", Constants.ACCEPT);
        headers.put("Authorization", "token " + PreferenceUtil.getInstance().getString(Constants.PreferenceKey.TOKEN));
        return headers;
    }
}
