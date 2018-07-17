package com.sunfusheng.github.net.download;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class DownloadManager {

    private static DownloadManager instance = new DownloadManager();

    private DownloadManager() {
    }

    public static DownloadManager instance() {
        return instance;
    }

    public void download(@NonNull String url, String filePath, IDownloadListener downloadListener) {
        Download.getService(downloadListener).download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnError(throwable -> {
                    if (downloadListener != null) {
                        downloadListener.onError(throwable);
                    }
                })
                .map(ResponseBody::byteStream)
                .observeOn(Schedulers.computation())
                .doOnNext(inputStream -> writeFile(inputStream, filePath))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver(filePath, downloadListener));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void writeFile(InputStream inputStream, String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        inputStream.close();
        fos.close();
    }
}
