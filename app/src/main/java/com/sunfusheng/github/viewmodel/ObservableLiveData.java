package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;

import com.sunfusheng.github.net.response.ResponseObserver;
import com.sunfusheng.github.net.response.ResponseResult;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author sunfusheng on 2018/4/17.
 */
public class ObservableLiveData<T> extends LiveData<ResponseResult<T>> {

    private final Observable<ResponseResult<T>> mObservable;
    private WeakReference<Disposable> mDisposableWeakReference;

    public static <T> LiveData<ResponseResult<T>> fromObservable(@NonNull Observable<ResponseResult<T>> observable) {
        return new ObservableLiveData<>(observable);
    }

    public ObservableLiveData(@NonNull Observable<ResponseResult<T>> observable) {
        this.mObservable = observable;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mObservable.subscribe(new ResponseObserver<T>() {
            @Override
            public void onNotify(ResponseResult<T> result) {
                postValue(result);
            }
        });
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        release();
    }

    public void release() {
        if (mDisposableWeakReference != null) {
            Disposable disposable = mDisposableWeakReference.get();
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            mDisposableWeakReference = null;
        }
    }
}
