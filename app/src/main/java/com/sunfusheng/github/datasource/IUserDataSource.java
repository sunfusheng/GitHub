package com.sunfusheng.github.datasource;

import android.arch.lifecycle.LiveData;

import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.ResponseResult;

/**
 * @author sunfusheng on 2018/4/13.
 */
public interface IUserDataSource {
    LiveData<ResponseResult<User>> getUser(String username);
}
