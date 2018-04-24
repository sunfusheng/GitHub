package com.sunfusheng.github.net.download;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class ProgressObserver implements Observer<InputStream> {

    private WeakReference<Disposable> disposableWeakReference;
    private String filePath;
    private IDownloadListener downloadListener;

    public ProgressObserver(String filePath, IDownloadListener downloadListener) {
        this.filePath = filePath;
        this.downloadListener = downloadListener;
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        disposableWeakReference = new WeakReference<>(disposable);
        if (downloadListener != null) {
            downloadListener.onStart();
        }
    }

    @Override
    public void onNext(InputStream inputStream) {
    }

    @Override
    public void onError(Throwable e) {
        release();
        if (downloadListener != null) {
            downloadListener.onError(e);
        }
    }

    @Override
    public void onComplete() {
        release();
        if (downloadListener != null) {
            downloadListener.onSuccess(new File(filePath));
        }
    }

    public void release() {
        if (disposableWeakReference != null) {
            Disposable disposable = disposableWeakReference.get();
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            disposableWeakReference = null;
        }
    }
}
