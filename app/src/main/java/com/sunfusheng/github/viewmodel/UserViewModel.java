package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sunfusheng.github.datasource.UserDataSource;
import com.sunfusheng.github.model.User;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserViewModel extends ViewModel {

    private LiveData<User> userLiveData;

    public LiveData<User> getUserLiveData(String username) {
        if (userLiveData == null) {
            userLiveData = UserDataSource.getInstance().getUser(username);
        }
        return userLiveData;
    }
}
