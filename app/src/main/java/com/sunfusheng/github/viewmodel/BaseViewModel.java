package com.sunfusheng.github.viewmodel;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.datasource.DataSourceHelper;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.http.response.ResponseObserver;
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
    private BaseDataSource<P, R> mDataSource;
    public LiveData<ResponseData<R>> liveData = Transformations.switchMap(mParams, params -> fetchData(
            mDataSource.localObservable(),
            mDataSource.remoteObservable(),
            params.fetchMode
    ));

    protected void request(@NonNull P params, @NonNull BaseDataSource<P, R> dataSource) {
        mDataSource = dataSource;
        mParams.setValue(params);
    }

    private ResponseData<R> mFirstNotify = null;

    private LiveData<ResponseData<R>> fetchData(
            @NonNull Observable<ResponseData<R>> localObservable,
            @NonNull Observable<ResponseData<R>> remoteObservable,
            @FetchMode int fetchMode) {

        MutableLiveData<ResponseData<R>> mutableLiveData = new MutableLiveData<>();
        Observable<ResponseData<R>> observable;
        if (fetchMode == FetchMode.LOCAL) {
            observable = localObservable;
        } else if (fetchMode == FetchMode.FORCE_REMOTE) {
            observable = remoteObservable
                    .switchIfEmpty(localObservable)
                    .onErrorResumeNext(localObservable);
        } else {
            observable = Observable.concat(localObservable, remoteObservable
                    .switchIfEmpty(localObservable)
                    .onErrorResumeNext(localObservable)
            );
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<R>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mFirstNotify = null;
                        super.onSubscribe(disposable);
                        ResponseData<R> loadingNotify = ResponseData.loading();
                        loadingNotify.setFetchMode(fetchMode == FetchMode.REMOTE ? FetchMode.LOCAL : fetchMode);
                        mutableLiveData.setValue(loadingNotify);
                    }

                    @Override
                    public void onNotify(ResponseData<R> responseData) {
                        if (DataSourceHelper.isSuccess(responseData)) {
                            loadSuccess(fetchMode);
                        } else if (DataSourceHelper.isError(responseData)) {
                            loadError(fetchMode, responseData.code, responseData.msg);
                        } else if (DataSourceHelper.isEmpty(responseData)) {
                            loadEmpty(fetchMode);
                        }

                        if (fetchMode == FetchMode.REMOTE) {
                            if (mFirstNotify == null) {
                                mFirstNotify = responseData;
                                if (DataSourceHelper.isSuccess(responseData)) {
                                    mutableLiveData.setValue(responseData);
                                }

                                if (!TextUtils.isEmpty(responseData.url)) {
                                    if (responseData.lastAccessTime > 0) {
                                        long betweenTime = (System.currentTimeMillis() - responseData.lastAccessTime) / 1000;
                                        if (betweenTime > responseData.localCacheValidateTime) {
                                            ResponseData<R> loadingNotify = ResponseData.loading();
                                            loadingNotify.setFetchMode(FetchMode.REMOTE);
                                            mutableLiveData.setValue(loadingNotify);
                                        }
                                    }
                                }
                            } else {
                                if (!DataSourceHelper.isSuccess(mFirstNotify) || (DataSourceHelper.isSuccess(responseData) && responseData.fetchMode != FetchMode.LOCAL)) {
                                    mutableLiveData.setValue(responseData);
                                }
                            }
                        } else {
                            mutableLiveData.setValue(responseData);
                        }
                    }
                });
        return mutableLiveData;
    }

    protected void loadSuccess(@FetchMode int fetchMode) {

    }

    protected void loadError(@FetchMode int fetchMode, int code, String msg) {

    }

    protected void loadEmpty(@FetchMode int fetchMode) {

    }
}
