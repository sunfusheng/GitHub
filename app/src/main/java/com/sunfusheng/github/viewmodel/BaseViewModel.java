package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.net.response.ResponseObserver;
import com.sunfusheng.github.net.response.ResponseData;
import com.sunfusheng.github.viewmodel.params.BaseParams;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * P for parameters
 * R for responses
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

    @FetchMode
    public int getRequestFetchMode() {
        P params = mParams.getValue();
        if (params != null) {
            return params.fetchMode;
        }
        return FetchMode.DEFAULT;
    }

    private LiveData<ResponseData<R>> fetchData(
            @NonNull Observable<ResponseData<R>> localObservable,
            @NonNull Observable<ResponseData<R>> remoteObservable,
            @FetchMode int fetchMode) {
        Log.d("sfs", "fetchData() fetchMode: " + ResponseData.getFetchModeString(fetchMode));

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
            MutableLiveData<ResponseData<R>> mutableLiveData = new MutableLiveData<>();
            Observable.concat(localObservable.doOnNext(it -> it.fetchMode = FetchMode.LOCAL),
                    remoteObservable.doOnNext(it -> it.fetchMode = FetchMode.REMOTE))
                    .subscribeOn(Schedulers.io())
                    .switchIfEmpty(Observable.just(ResponseData.empty()))
                    .onErrorResumeNext(throwable -> {
                        return Observable.just(ResponseData.error(throwable));
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<R>() {
                        @Override
                        public void onNotify(ResponseData<R> result) {
                            mutableLiveData.setValue(result);
                        }
                    });
            return mutableLiveData;
        }
    }
}
