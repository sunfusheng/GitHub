package com.sunfusheng.github.net.api;

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
        Log.d("--->", "onSubscribe() 【loading】");
        onNotify(ResponseResult.loading());
    }

    @Override
    public void onNext(ResponseResult<T> t) {
        isOnNext = true;
        if (t == null) {
            Log.d("--->", "onNext() 【empty】");
            onNotify(ResponseResult.empty());
        } else {
            Log.d("--->", "onNext() 【success】 :" + t.toString());
            onNotify(t);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        release();
        Log.d("--->", "onError() 【error】 :" + ResponseResult.error(throwable).errorString());
        onNotify(ResponseResult.error(throwable));
    }

    @Override
    public void onComplete() {
        release();
        if (!isOnNext) {
            Log.d("--->", "onComplete() 【empty】");
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

    public abstract void onNotify(ResponseResult<T> result);
}
