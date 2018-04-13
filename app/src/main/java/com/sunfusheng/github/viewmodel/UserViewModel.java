package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.UserDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.ResponseResult;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserViewModel extends ViewModel {

    private LiveData<ResponseResult<User>> userLiveData;

    public LiveData<ResponseResult<User>> getUser(String username) {
        return UserDataSource.instance().getUser(username, FetchMode.REMOTE);
    }

    public LiveData<ResponseResult<User>> getUser(String username, @FetchMode int fetchMode) {
        return UserDataSource.instance().getUser(username, fetchMode);
    }
}
