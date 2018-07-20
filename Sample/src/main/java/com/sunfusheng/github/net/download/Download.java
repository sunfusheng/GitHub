package com.sunfusheng.github.net.download;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.factory.RetrofitFactory;
import com.sunfusheng.github.net.interceptor.DownloadInterceptor;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class Download {

    private static DownloadService downloadService;

    private Download() {
    }

    public static DownloadService getService(IDownloadListener downloadListener) {
        if (downloadService == null) {
            downloadService = RetrofitFactory.getService(FetchMode.REMOTE, DownloadService.class, new DownloadInterceptor(downloadListener));
        }
        return downloadService;
    }
}
