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
import okhttp3.OkHttpClient;
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
        Retrofit retrofit = mRetrofitMap.get(key.toString());
        if (retrofit == null) {
            OkHttpClient okHttpClient = OkHttpClientFactory.create(interceptors);
            retrofit = RetrofitFactory.create(okHttpClient, baseUrl, isJson);
            mRetrofitMap.put(key.toString(), retrofit);
        }
        return retrofit.create(service);
    }

    public static <T> T getCommonService(Class<T> service, Interceptor... interceptors) {
        return getService(Constants.BASE_URL_API_GITHUB, true, service, interceptors);
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
        return getService(Constants.BASE_URL_GITHUB, false, WebPageService.class, (Interceptor[]) null);
    }
}
