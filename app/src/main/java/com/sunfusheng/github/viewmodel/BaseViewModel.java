package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.net.response.ResponseData;
import com.sunfusheng.github.net.response.ResponseObserver;
import com.sunfusheng.github.viewmodel.params.BaseParams;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * P: for parameters
 * R: for responses
 *
 * @author sunfusheng on 2018/4/28.
 */
abstract public class BaseViewModel<P extends BaseParams, R> extends ViewModel {
    private MutableLiveData<P> mParams = new MutableLiveData<>();
    private BaseDataSource<R> mDataSource;
    public LiveData<ResponseData<R>> liveData = Transformations.switchMap(mParams, params -> fetchData(
            mDataSource.localObservable(),
            mDataSource.remoteObservable(),
            params.fetchMode
    ));

    protected void request(@NonNull P params, @NonNull BaseDataSource<R> dataSource) {
        Log.d("sfs", "request(): " + params.toString());
        mDataSource = dataSource;
        mParams.setValue(params);
    }

    private LiveData<ResponseData<R>> fetchData(
            @NonNull Observable<ResponseData<R>> localObservable,
            @NonNull Observable<ResponseData<R>> remoteObservable,
            @FetchMode int fetchMode) {

        MutableLiveData<ResponseData<R>> mutableLiveData = new MutableLiveData<>();
        Observable<ResponseData<R>> observable;
        if (fetchMode == FetchMode.LOCAL) {
            observable = localObservable;
        } else if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
            observable = remoteObservable.switchIfEmpty(localObservable)
                    .onErrorResumeNext(localObservable);
        } else {
            observable = Observable.concat(
                    localObservable,
                    remoteObservable.switchIfEmpty(localObservable)
                            .onErrorResumeNext(localObservable)
            );
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<R>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        super.onSubscribe(disposable);
                        @FetchMode int realFetchMode = fetchMode;
                        if (fetchMode == FetchMode.DEFAULT) {

                        }
                        ResponseData<R> loadingResponseData = ResponseData.loading();
                        loadingResponseData.setFetchMode(realFetchMode);
                        mutableLiveData.setValue(loadingResponseData);
                    }

                    @Override
                    public void onNotify(ResponseData<R> result) {
                        mutableLiveData.setValue(result);
                    }
                });
        return mutableLiveData;
    }
}
