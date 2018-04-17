package com.sunfusheng.github.net;

import com.sunfusheng.github.util.ExceptionUtil;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/17.
 */
public abstract class CommonObserver<T> implements Observer<T> {

    private WeakReference<Disposable> disposableWeakReference;

    @Override
    public void onSubscribe(Disposable disposable) {
        disposableWeakReference = new WeakReference<>(disposable);
        onNotify(ResponseResult.loading(), disposable);
    }

    @Override
    public void onNext(T t) {
        if (t == null) {
            onNotify(ResponseResult.empty(), getDisposable());
        }

        if (t instanceof Response) {
            Response response = (Response) t;
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    onNotify(ResponseResult.success(response), getDisposable());
                } else {
                    onNotify(ResponseResult.empty(), getDisposable());
                }
            }
        } else {
            onNotify(ResponseResult.success(t), getDisposable());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        release();
        onNotify(ResponseResult.error(ExceptionUtil.handleException(throwable)), getDisposable());
    }

    @Override
    public void onComplete() {
        release();
    }

    public Disposable getDisposable() {
        if (disposableWeakReference != null) {
            return disposableWeakReference.get();
        }
        return null;
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

    public abstract void onNotify(ResponseResult<T> result, Disposable disposable);
}
