package com.sunfusheng.github.net.api;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.ApiService;
import com.sunfusheng.github.net.factory.RetrofitFactory;
import com.sunfusheng.github.net.interceptor.CommonHeaderInterceptor;
import com.sunfusheng.github.net.interceptor.LoginHeaderInterceptor;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {

    private static ApiService localCommonService;
    private static ApiService remoteCommonService;
    private static ApiService loginService;

    private static final Api instance = new Api();

    private Api() {
    }

    public static Api getInstance() {
        return instance;
    }

    public static ApiService getLocalCommonService() {
        if (localCommonService == null) {
            localCommonService = RetrofitFactory.getService(FetchMode.LOCAL, ApiService.class, new CommonHeaderInterceptor());
        }
        return localCommonService;
    }

    public static ApiService getRemoteCommonService() {
        if (remoteCommonService == null) {
            remoteCommonService = RetrofitFactory.getService(FetchMode.REMOTE, ApiService.class, new CommonHeaderInterceptor());
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

    public static ApiService getLoginService() {
        if (loginService == null) {
            loginService = RetrofitFactory.getService(FetchMode.REMOTE, ApiService.class, new LoginHeaderInterceptor());
        }
        return loginService;
    }
}
