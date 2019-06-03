package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.datasource.DataSourceHelper;
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
                        mFirstNotify = null;
                        ResponseData<R> loadingNotify = ResponseData.loading();
                        loadingNotify.setFetchMode(fetchMode == FetchMode.REMOTE ? FetchMode.LOCAL : fetchMode);
                        mutableLiveData.setValue(loadingNotify);
                    }

                    @Override
                    public void onNotify(ResponseData<R> notify) {
                        if (fetchMode == FetchMode.REMOTE) {
                            if (mFirstNotify == null) {
                                mFirstNotify = notify;
                                if (DataSourceHelper.isSuccess(notify)) {
                                    mutableLiveData.setValue(notify);
                                }

                                if (!TextUtils.isEmpty(notify.url)) {
                                    if (notify.lastAccessTime > 0) {
                                        long betweenTime = (System.currentTimeMillis() - notify.lastAccessTime) / 1000;
                                        if (betweenTime > notify.localCacheValidateTime) {
                                            ResponseData<R> loadingNotify = ResponseData.loading();
                                            loadingNotify.setFetchMode(FetchMode.REMOTE);
                                            mutableLiveData.setValue(loadingNotify);
                                        }
                                        Log.d("sfs", "notify.url: " + notify.url + " betweenTime: " + betweenTime + " localCacheValidateTime: " + notify.localCacheValidateTime);
                                    }
                                }
                            } else {
                                if (!DataSourceHelper.isSuccess(mFirstNotify) || (DataSourceHelper.isSuccess(notify) && notify.fetchMode != FetchMode.LOCAL)) {
                                    mutableLiveData.setValue(notify);
                                }
                            }
                        } else {
                            mutableLiveData.setValue(notify);
                        }
                    }
                });
        return mutableLiveData;
    }
}
