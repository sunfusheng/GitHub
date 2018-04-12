package com.sunfusheng.github.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.SystemClock;

import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.exception.ExceptionUtil;
import com.sunfusheng.github.net.exception.ResponseException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserDataSource {

    private static UserDataSource dataSource = new UserDataSource();

    private UserDataSource() {
    }

    public static UserDataSource getInstance() {
        return dataSource;
    }

    public LiveData<User> getUser(String username) {
        MutableLiveData<User> mutableLiveData = new MutableLiveData<>();
        Api.getAuthService().fetchUser(username)
                .subscribeOn(Schedulers.io())
                .map(it -> {
                    SystemClock.sleep(5000);
                    return it;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mutableLiveData::setValue, throwable -> {
                    throwable.printStackTrace();
                    ResponseException responseException = ExceptionUtil.checkException(throwable);
                });
        return mutableLiveData;
    }
}
