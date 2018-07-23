package com.sunfusheng.github.datasource;

import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.response.ResponseResult;

import io.reactivex.Observable;

/**
 * @author sunfusheng on 2018/4/13.
 */
public interface IUserDataSource {
    Observable<ResponseResult<User>> getUser(String username);
}
