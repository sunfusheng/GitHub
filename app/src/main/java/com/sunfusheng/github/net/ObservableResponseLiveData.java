package com.sunfusheng.github.net;

import android.arch.lifecycle.LiveData;

import com.sunfusheng.github.util.ExceptionUtil;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class ObservableResponseLiveData<T> extends LiveData<ResponseResult<T>> {

    private final Observable<Response<T>> observable;
    private WeakReference<Disposable> disposableWeakReference;

    public static <T> LiveData<ResponseResult<T>> fromObservable(@NonNull Observable<Response<T>> observable) {
        return new ObservableResponseLiveData<>(observable);
    }

    public ObservableResponseLiveData(@NonNull Observable<Response<T>> observable) {
        this.observable = observable;
    }

    @Override
    protected void onActive() {
        super.onActive();
        observable.subscribe(new Observer<Response<T>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                disposableWeakReference = new WeakReference<>(disposable);
                postValue(ResponseResult.loading());
            }

            @Override
            public void onNext(Response<T> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        postValue(ResponseResult.success(response));
                    } else {
                        postValue(ResponseResult.empty());
                    }
                } else {
                    postValue(ResponseResult.error(ExceptionUtil.handleException(response)));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                release();
                postValue(ResponseResult.error(ExceptionUtil.handleException(throwable)));
            }

            @Override
            public void onComplete() {
                release();
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
