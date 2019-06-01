package com.sunfusheng.github.net;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.net.download.IDownloadListener;
import com.sunfusheng.github.net.factory.OkHttpClientFactory;
import com.sunfusheng.github.net.factory.RetrofitFactory;
import com.sunfusheng.github.net.interceptor.DownloadInterceptor;
import com.sunfusheng.github.net.interceptor.LoginHeaderInterceptor;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * @author by sunfusheng on 2018/4/8.
 */
public class Api {
    private static final Api sInstance = new Api();
    private static Map<String, Retrofit> mRetrofitMap = new HashMap<>();

    public static Api getInstance() {
        return sInstance;
    }

    private Api() {
    }

    private static <T> T getService(String baseUrl, boolean isJson, Class<T> service, Interceptor... interceptors) {
        StringBuilder key = new StringBuilder();
        key.append(baseUrl);
        key.append("_").append(isJson);
        key.append("_").append(service.getSimpleName());
        Retrofit retrofit = null;//mRetrofitMap.get(key.toString());
        if (retrofit == null) {
            retrofit = RetrofitFactory.create(OkHttpClientFactory.create(interceptors), baseUrl, isJson);
            mRetrofitMap.put(key.toString(), retrofit);
        }
        return retrofit.create(service);
    }

    public static <T> T getCommonService(Class<T> service, Interceptor... interceptors) {
        return getService(Constants.BASE_URL, true, service, interceptors);
    }

    public static LoginService getLoginService() {
        return getCommonService(LoginService.class, new LoginHeaderInterceptor());
    }

    public static DownloadService getDownloadService(IDownloadListener downloadListener) {
        return getCommonService(DownloadService.class, new DownloadInterceptor(downloadListener));
    }

    public static CommonService getCommonService() {
        return getCommonService(CommonService.class, (Interceptor[]) null);
    }

    public static WebPageService getWebPageService() {
        return getService(Constants.BASE_WEB_PAGE_URL, false, WebPageService.class, (Interceptor[]) null);
    }
}
