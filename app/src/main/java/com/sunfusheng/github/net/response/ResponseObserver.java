package com.sunfusheng.github.net.response;

import android.util.Log;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author sunfusheng on 2018/4/17.
 */
public abstract class ResponseObserver<T> implements Observer<ResponseData<T>> {

    private WeakReference<Disposable> mDisposableWeakRef;
    private boolean isOnNext;

    @Override
    public void onSubscribe(Disposable disposable) {
        isOnNext = false;
        mDisposableWeakRef = new WeakReference<>(disposable);
    }

    @Override
    public void onNext(ResponseData<T> t) {
        isOnNext = true;
        if (t == null) {
            Log.w("sfs", "ResponseObserver onNext(): EMPTY");
            onNotify(ResponseData.empty());
        } else {
            onNotify(t);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        release();
        Log.e("sfs", "ResponseObserver onError(): " + ResponseData.error(throwable).errorString());
        onNotify(ResponseData.error(throwable));
    }

    @Override
    public void onComplete() {
        release();
        if (!isOnNext) {
            Log.w("sfs", "ResponseObserver onComplete(): EMPTY");
            onNotify(ResponseData.empty());
        }
    }

    private void release() {
        if (mDisposableWeakRef != null) {
            Disposable disposable = mDisposableWeakRef.get();
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            mDisposableWeakRef = null;
        }
    }

    public abstract void onNotify(ResponseData<T> notify);
}
