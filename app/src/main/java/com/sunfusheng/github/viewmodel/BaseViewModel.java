package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.net.response.ResponseObserver;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.github.viewmodel.params.BaseParams;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/28.
 */
public abstract class BaseViewModel<P extends BaseParams, T> extends ViewModel {
    private final MutableLiveData<P> mParams = new MutableLiveData<>();
    private BaseDataSource mDataSource;

    public final LiveData<ResponseResult<T>> liveData = Transformations.switchMap(mParams, params ->
            fetchData(mDataSource.localObservable(), mDataSource.remoteObservable(), params.fetchMode));

    protected void request(@NonNull P params, @NonNull BaseDataSource<T> dataSource) {
        mDataSource = dataSource;
        mParams.setValue(params);
    }

    public @FetchMode
    int getRequestFetchMode() {
        P params = mParams.getValue();
        if (params != null) {
            return params.fetchMode;
        }
        return FetchMode.DEFAULT;
    }

    protected <T> LiveData<ResponseResult<T>> fetchData(
            @NonNull Observable<ResponseResult<T>> localObservable,
            @NonNull Observable<ResponseResult<T>> remoteObservable,
            @FetchMode int fetchMode) {
        if (fetchMode == FetchMode.LOCAL) {
            return ObservableLiveData.fromObservable(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL));
        } else if (fetchMode == FetchMode.REMOTE) {
            return ObservableLiveData.fromObservable(remoteObservable.doOnNext(it -> it.fetchMode = FetchMode.REMOTE)
                    .switchIfEmpty(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL))
                    .onErrorResumeNext(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL))
            );
        } else if (fetchMode == FetchMode.FORCE_REMOTE) {
            return ObservableLiveData.fromObservable(remoteObservable.doOnNext(it -> it.fetchMode = FetchMode.FORCE_REMOTE)
                    .switchIfEmpty(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL))
                    .onErrorResumeNext(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL))
            );
        } else {
            MutableLiveData<ResponseResult<T>> mutableLiveData = new MutableLiveData<>();
            Observable.concat(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL),
                    remoteObservable.doOnNext(it -> it.fetchMode = FetchMode.REMOTE))
                    .subscribeOn(Schedulers.io())
                    .switchIfEmpty(Observable.just(ResponseResult.empty()))
                    .onErrorResumeNext(throwable -> {
                        return Observable.just(ResponseResult.error(throwable));
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<T>() {
                        @Override
                        public void onNotify(ResponseResult<T> result) {
                            mutableLiveData.setValue(result);
                        }
                    });
            return mutableLiveData;
        }
    }
}
