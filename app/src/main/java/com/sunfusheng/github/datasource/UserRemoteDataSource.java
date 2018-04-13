package com.sunfusheng.github.datasource;

import android.arch.lifecycle.LiveData;
import android.os.SystemClock;

import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.ObservableResponseLiveData;
import com.sunfusheng.github.net.ResponseResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserRemoteDataSource implements IUserDataSource {

    private static UserRemoteDataSource instance = new UserRemoteDataSource();

    private UserRemoteDataSource() {
    }

    public static UserRemoteDataSource instance() {
        return instance;
    }

    public LiveData<ResponseResult<User>> getUser(String username) {
        return ObservableResponseLiveData.fromObservable(
                Api.getAuthService().fetchUser(username)
                        .subscribeOn(Schedulers.io())
                        .map(it -> {
                            SystemClock.sleep(3000);
                            return it;
                        }).observeOn(AndroidSchedulers.mainThread())
        );
    }
}
