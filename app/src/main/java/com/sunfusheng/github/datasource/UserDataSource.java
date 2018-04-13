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
 * @author sunfusheng on 2018/4/12.
 */
public class UserDataSource {

    private static UserDataSource dataSource = new UserDataSource();

    private UserDataSource() {
    }

    public static UserDataSource getInstance() {
        return dataSource;
    }

    public LiveData<ResponseResult<User>> getUser(String username) {
        return ObservableResponseLiveData.from(Api.getAuthService().fetchUser(username)
                .subscribeOn(Schedulers.io())
                .map(it -> {
                    SystemClock.sleep(3000);
                    return it;
                })
                .filter(it -> it != null)
                .observeOn(AndroidSchedulers.mainThread()));
    }
}
