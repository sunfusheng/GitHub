package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.UserDetailDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.response.ResponseResult;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserDetailViewModel extends BaseViewModel {

    public final LiveData<ResponseResult<User>> liveData =
            Transformations.switchMap(mParams, params -> fetchUserDetail(params.username, params.fetchMode));

    public void request(String username, @FetchMode int fetchMode) {
        mParams.setValue(new RequestParams(username, fetchMode));
    }

    private LiveData<ResponseResult<User>> fetchUserDetail(String username, @FetchMode int fetchMode) {
        UserDetailDataSource dataSource = new UserDetailDataSource(username);
        return fetchData(
                dataSource.localObservable(),
                dataSource.remoteObservable(),
                fetchMode
        );
    }
}
