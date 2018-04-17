package com.sunfusheng.github.net;

import com.sunfusheng.github.util.ExceptionUtil;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author sunfusheng on 2018/4/17.
 */
public abstract class CommonObserver<T> implements Observer<ResponseResult<T>> {

    private WeakReference<Disposable> disposableWeakReference;

    @Override
    public void onSubscribe(Disposable disposable) {
        disposableWeakReference = new WeakReference<>(disposable);
        onNotify(ResponseResult.loading());
    }

    @Override
    public void onNext(ResponseResult<T> t) {
        if (t == null || t.data == null) {
            onNotify(ResponseResult.empty());
        }
        onNotify(t);
    }

    @Override
    public void onError(Throwable throwable) {
        release();
        onNotify(ResponseResult.error(ExceptionUtil.handleException(throwable)));
    }

    @Override
    public void onComplete() {
        release();
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

    public abstract void onNotify(ResponseResult<T> result);
}
