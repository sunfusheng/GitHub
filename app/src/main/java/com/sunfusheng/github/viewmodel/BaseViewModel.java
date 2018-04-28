package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.api.ResponseObserver;
import com.sunfusheng.github.net.api.ResponseResult;
import com.sunfusheng.github.util.NetworkUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/28.
 */
public class BaseViewModel extends ViewModel {

    protected final MutableLiveData<RequestParams> requestParams = new MutableLiveData();

    public static class RequestParams {
        public String username;
        public int page;
        public int perPage = Constants.PER_PAGE_30;
        public int fetchMode;

        public RequestParams(String username, int fetchMode) {
            this(username, 1, Constants.PER_PAGE_30, fetchMode);
        }

        public RequestParams(String username, int page, int fetchMode) {
            this(username, page, Constants.PER_PAGE_30, fetchMode);
        }

        public RequestParams(String username, int page, int perPage, int fetchMode) {
            this.username = username;
            this.page = page;
            this.perPage = perPage;
            this.fetchMode = fetchMode;
        }
    }

    protected  <T> LiveData<ResponseResult<T>> fetchData(@NonNull Observable<ResponseResult<T>> localObservable,
                                                      @NonNull Observable<ResponseResult<T>> remoteObservable,
                                                      @FetchMode int fetchMode) {
        if (fetchMode == FetchMode.LOCAL || !NetworkUtil.isConnected()) {
            return ObservableLiveData.fromObservable(localObservable);
        } else if (fetchMode == FetchMode.REMOTE) {
            return ObservableLiveData.fromObservable(remoteObservable
                    .switchIfEmpty(localObservable)
                    .onErrorResumeNext(localObservable)
            );
        } else {
            MutableLiveData<ResponseResult<T>> mutableLiveData = new MutableLiveData<>();

            Observable.concat(localObservable, remoteObservable)
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
