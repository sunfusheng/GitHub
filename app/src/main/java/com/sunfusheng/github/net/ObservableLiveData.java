package com.sunfusheng.github.net;

import android.arch.lifecycle.LiveData;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author sunfusheng on 2018/4/17.
 */
public class ObservableLiveData<T> extends LiveData<ResponseResult<T>> {

    private final Observable<T> observable;
    private WeakReference<Disposable> disposableWeakReference;

    public static <T> LiveData<ResponseResult<T>> fromObservable(@NonNull Observable<T> observable) {
        return new ObservableLiveData<>(observable);
    }

    public ObservableLiveData(@NonNull Observable<T> observable) {
        this.observable = observable;
    }

    @Override
    protected void onActive() {
        super.onActive();
        observable.subscribe(new CommonObserver<T>() {
            @Override
            public void onNotify(ResponseResult<T> result, Disposable disposable) {
                disposableWeakReference = new WeakReference<>(disposable);
            }
        });
//        observable.subscribe(new Observer<T>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                disposableWeakReference = new WeakReference<>(disposable);
//                postValue(ResponseResult.loading());
//            }
//
//            @Override
//            public void onNext(T response) {
//                if (response != null) {
//                    postValue(new ResponseResult<>(QueryResult.EXIST, "exist", response, LoadingState.SUCCESS));
//                } else {
//                    postValue(new ResponseResult<>(QueryResult.NONE, "not exist", null, LoadingState.EMPTY));
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                release();
//                postValue(ResponseResult.error(ExceptionUtil.handleException(throwable)));
//            }
//
//            @Override
//            public void onComplete() {
//                release();
//            }
//        });
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
