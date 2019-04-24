package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.UserLocalDataSource;
import com.sunfusheng.github.datasource.UserRemoteDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.response.ResponseResult;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserViewModel extends BaseViewModel {

    public final LiveData<ResponseResult<User>> liveData =
            Transformations.switchMap(requestParams, input -> getUser(input.username, input.fetchMode));

    public void setRequestParams(String username, @FetchMode int fetchMode) {
        requestParams.setValue(new RequestParams(username, fetchMode));
    }

    private LiveData<ResponseResult<User>> getUser(String username, @FetchMode int fetchMode) {
        return fetchData(UserLocalDataSource.instance().getUser(username),
                UserRemoteDataSource.instance().getUser(username),
                fetchMode);
    }
}
