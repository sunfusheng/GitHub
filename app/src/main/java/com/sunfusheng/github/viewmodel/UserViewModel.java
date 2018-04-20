package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.UserLocalDataSource;
import com.sunfusheng.github.datasource.UserRemoteDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.ResponseObserver;
import com.sunfusheng.github.net.ResponseResult;
import com.sunfusheng.github.util.NetworkUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserViewModel extends ViewModel {

    private final MutableLiveData<RequestParams> params = new MutableLiveData();

    public final LiveData<ResponseResult<User>> liveData =
            Transformations.switchMap(params, input -> getUser(input.username, input.fetchMode));

    public void setRequestParams(String username, @FetchMode int fetchMode) {
        params.setValue(new RequestParams(username, fetchMode));
    }

    public static class RequestParams {
        public String username;
        public int fetchMode;

        public RequestParams(String username, int fetchMode) {
            this.username = username;
            this.fetchMode = fetchMode;
        }
    }

    private LiveData<ResponseResult<User>> getUser(String username, @FetchMode int fetchMode) {
        if (fetchMode == FetchMode.LOCAL || !NetworkUtil.isConnected()) {
            return ObservableLiveData.fromObservable(UserLocalDataSource.instance().getUser(username));
        } else if (fetchMode == FetchMode.REMOTE) {
            return ObservableLiveData.fromObservable(UserRemoteDataSource.instance().getUser(username)
                    .switchIfEmpty(UserLocalDataSource.instance().getUser(username))
                    .onErrorResumeNext(UserLocalDataSource.instance().getUser(username))
            );
        } else {
            MutableLiveData<ResponseResult<User>> mutableLiveData = new MutableLiveData<>();

            Observable.concat(UserLocalDataSource.instance().getUser(username), UserRemoteDataSource.instance().getUser(username))
                    .subscribeOn(Schedulers.io())
                    .switchIfEmpty(Observable.just(ResponseResult.empty()))
                    .onErrorResumeNext(throwable -> {
                        return Observable.just(ResponseResult.error(throwable));
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<User>() {
                        @Override
                        public void onNotify(ResponseResult<User> result) {
                            mutableLiveData.setValue(result);
                        }
                    });

            return mutableLiveData;
        }
    }

}
