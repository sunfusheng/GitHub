package com.sunfusheng.github.datasource;

import android.arch.lifecycle.LiveData;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.ResponseResult;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserDataSource {

    private static UserDataSource instance = new UserDataSource();

    private UserDataSource() {
    }

    public static UserDataSource instance() {
        return instance;
    }

    public LiveData<ResponseResult<User>> getUser(String username, @FetchMode int fetchMode) {
        switch (fetchMode) {
            case FetchMode.DEFAULT:
                return UserRemoteDataSource.instance().getUser(username);
            case FetchMode.LOCAL:
                return UserLocalDataSource.instance().getUser(username);
            default:
            case FetchMode.REMOTE:
                return UserRemoteDataSource.instance().getUser(username);
        }
    }
}
