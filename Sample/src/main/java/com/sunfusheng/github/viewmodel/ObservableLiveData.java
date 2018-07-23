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

    private final Observable<ResponseResult<T>> observable;
    private WeakReference<Disposable> disposableWeakReference;

    public static <T> LiveData<ResponseResult<T>> fromObservable(@NonNull Observable<ResponseResult<T>> observable) {
        return new ObservableLiveData<>(observable);
    }

    public ObservableLiveData(@NonNull Observable<ResponseResult<T>> observable) {
        this.observable = observable;
    }

    @Override
    protected void onActive() {
        super.onActive();
        observable.subscribe(new ResponseObserver<T>() {
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
        if (disposableWeakReference != null) {
            Disposable disposable = disposableWeakReference.get();
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
            disposableWeakReference = null;
        }
    }
}
