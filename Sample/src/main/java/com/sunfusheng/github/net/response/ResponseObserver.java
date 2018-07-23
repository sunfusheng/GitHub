package com.sunfusheng.github.net.response;

import android.util.Log;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author sunfusheng on 2018/4/17.
 */
public abstract class ResponseObserver<T> implements Observer<ResponseResult<T>> {

    private WeakReference<Disposable> disposableWeakReference;
    private boolean isOnNext;

    @Override
    public void onSubscribe(Disposable disposable) {
        isOnNext = false;
        disposableWeakReference = new WeakReference<>(disposable);
        Log.d("--->", "onSubscribe()【loading】 hashCode:" + getDisposableHashCode());
        onNotify(ResponseResult.loading());
    }

    @Override
    public void onNext(ResponseResult<T> t) {
        isOnNext = true;
        if (t == null) {
            Log.d("--->", "onNext()【empty】 hashCode:" + getDisposableHashCode());
            onNotify(ResponseResult.empty());
        } else {
            Log.d("--->", "onNext()【success】 fetchMode:" + t.fetchMode + " hashCode:" + getDisposableHashCode());
            onNotify(t);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        release();
        Log.d("--->", "onError()【error】 hashCode:" + getDisposableHashCode() + " error info:" + ResponseResult.error(throwable).errorString());
        onNotify(ResponseResult.error(throwable));
    }

    @Override
    public void onComplete() {
        release();
        if (!isOnNext) {
            Log.d("--->", "onComplete()【empty】 hashCode:" + getDisposableHashCode());
            onNotify(ResponseResult.empty());
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

    public int getDisposableHashCode() {
        if (disposableWeakReference != null) {
            Disposable disposable = disposableWeakReference.get();
            if (disposable != null) {
                return disposable.hashCode();
            }
        }
        return 0;
    }

    public abstract void onNotify(ResponseResult<T> result);
}
