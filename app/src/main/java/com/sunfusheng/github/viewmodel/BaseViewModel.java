package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.response.ResponseObserver;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.github.util.NetworkUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/28.
 */
public class BaseViewModel extends ViewModel {
    protected final MutableLiveData<RequestParams> mParams = new MutableLiveData<>();

    public static class RequestParams {
        public String username;
        public int page;
        public int pageCount;
        public int fetchMode;

        public RequestParams(String username, int fetchMode) {
            this(username, 1, Constants.PAGE_COUNT, fetchMode);
        }

        public RequestParams(String username, int page, int fetchMode) {
            this(username, page, Constants.PAGE_COUNT, fetchMode);
        }

        public RequestParams(String username, int page, int pageCount, int fetchMode) {
            this.username = username;
            this.page = page;
            this.pageCount = pageCount;
            this.fetchMode = NetworkUtil.isConnected() ? fetchMode : FetchMode.LOCAL;
        }
    }

    public RequestParams getRequestParams() {
        return mParams.getValue();
    }

    @FetchMode
    public int getRequestFetchMode() {
        if (getRequestParams() != null) {
            return getRequestParams().fetchMode;
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
