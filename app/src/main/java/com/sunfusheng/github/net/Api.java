package com.sunfusheng.github.net;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.factory.OkHttpClientFactory;
import com.sunfusheng.github.net.factory.RetrofitFactory;
import com.sunfusheng.github.net.interceptor.LoginHeaderInterceptor;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {

    private static final Api instance = new Api();
    private static Map<String, Retrofit> retrofitMap = new HashMap<>();

    public static Api getInstance() {
        return instance;
    }

    private Api() {
    }

    private static <T> T getService(String baseUrl, boolean isJson, Class<T> service, @FetchMode int fetchMode, Interceptor... interceptors) {
//        StringBuilder key = new StringBuilder();
//        key.append(baseUrl);
//        key.append("_").append(isJson);
//        key.append("_").append(service.getSimpleName());
//        key.append("_").append(fetchMode);
//        Retrofit retrofit = retrofitMap.get(key.toString());
//        if (retrofit == null) {
//            retrofit = RetrofitFactory.create(OkHttpClientFactory.create(fetchMode, interceptors), baseUrl, isJson);
//            retrofitMap.put(key.toString(), retrofit);
//        }

        Retrofit retrofit = RetrofitFactory.create(OkHttpClientFactory.create(fetchMode, interceptors), baseUrl, isJson);
        return retrofit.create(service);
    }

    public static <T> T getCommonService(Class<T> service, @FetchMode int fetchMode, Interceptor... interceptors) {
        return getService(Constants.BASE_URL, true, service, fetchMode, interceptors);
    }

    public static CommonService getLoginService() {
        return getCommonService(CommonService.class, FetchMode.REMOTE, new LoginHeaderInterceptor());
    }

    public static CommonService getCommonService(@FetchMode int fetchMode) {
        return getCommonService(CommonService.class, fetchMode, (Interceptor[]) null);
    }

    public static CommonService getCommonService() {
        return getCommonService(FetchMode.REMOTE);
    }

    public static <T> T getWebPageService(Class<T> service, @FetchMode int fetchMode, Interceptor... interceptors) {
        return getService(Constants.BASE_WEB_PAGE_URL, false, service, fetchMode, interceptors);
    }

    public static WebPageService getWebPageService() {
        return getWebPageService(WebPageService.class, FetchMode.REMOTE, (Interceptor[]) null);
    }
}
