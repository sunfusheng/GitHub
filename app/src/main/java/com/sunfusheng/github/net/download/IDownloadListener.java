package com.sunfusheng.github.net.download;

import java.io.File;

/**
 * @author sunfusheng on 2018/4/24.
 */
public interface IDownloadListener {
    void onStart();

    void onSuccess(File file);

    void onError(Throwable e);

    void onProgress(long bytesTransferred, long totalBytes, int percentage);
}