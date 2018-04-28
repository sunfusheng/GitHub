package com.sunfusheng.github.net.api;

import com.sunfusheng.github.net.factory.RetrofitFactory;
import com.sunfusheng.github.net.interceptor.CommonHeaderInterceptor;
import com.sunfusheng.github.net.interceptor.LoginHeaderInterceptor;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {

    private static ApiService apiCommonService;
    private static ApiService apiLoginService;

    private Api() {
    }

    public static ApiService getCommonService() {
        if (apiCommonService == null) {
            apiCommonService = RetrofitFactory.getService(ApiService.class, new CommonHeaderInterceptor());
        }
        return apiCommonService;
    }

    public static ApiService getLoginService() {
        if (apiLoginService == null) {
            apiLoginService = RetrofitFactory.getService(ApiService.class, new LoginHeaderInterceptor());
        }
        return apiLoginService;
    }
}
