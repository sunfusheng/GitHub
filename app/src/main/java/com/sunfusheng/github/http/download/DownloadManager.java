package com.sunfusheng.github.http.download;

import com.sunfusheng.github.http.Api;

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

    public void download(String url, String filePath, IDownloadListener downloadListener) {
        Api.getDownloadService(downloadListener).download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnError(throwable -> {
                    if (downloadListener != null) {
                        AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                            downloadListener.onError(throwable);
                        });
                    }
                })
                .map(ResponseBody::byteStream)
                .observeOn(Schedulers.io())
                .doOnNext(inputStream -> writeFile(inputStream, filePath))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver(filePath, downloadListener));
    }

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
