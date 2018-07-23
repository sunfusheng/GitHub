package com.sunfusheng.github.net;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.factory.OkHttpClientFactory;
import com.sunfusheng.github.net.factory.RetrofitFactory;
import com.sunfusheng.github.net.interceptor.CommonHeaderInterceptor;
import com.sunfusheng.github.net.interceptor.LoginHeaderInterceptor;

import okhttp3.Interceptor;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {
    private static ApiService remoteCommonService;

    private static final Api instance = new Api();

    private Api() {
    }

    public static Api getInstance() {
        return instance;
    }

    public static <T> T getService(Class<T> service, @FetchMode int fetchMode, Interceptor... interceptors) {
        return RetrofitFactory.create(OkHttpClientFactory.create(fetchMode, interceptors)).create(service);
    }

    public static ApiService getLoginService() {
        return getService(ApiService.class, FetchMode.REMOTE, new LoginHeaderInterceptor());
    }

    private static ApiService getLocalCommonService() {
        return getService(ApiService.class, FetchMode.LOCAL, new CommonHeaderInterceptor());
    }

    private static synchronized ApiService getRemoteCommonService() {
        if (remoteCommonService == null) {
            remoteCommonService = getService(ApiService.class, FetchMode.REMOTE, new CommonHeaderInterceptor());
        }
        return remoteCommonService;
    }

    public static ApiService getCommonService(@FetchMode int fetchMode) {
        if (fetchMode == FetchMode.LOCAL) {
            return getLocalCommonService();
        }
        return getRemoteCommonService();
    }

    public static ApiService getCommonService() {
        return getRemoteCommonService();
    }
}
