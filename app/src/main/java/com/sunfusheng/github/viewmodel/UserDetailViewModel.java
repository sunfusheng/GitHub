package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.UserDetailDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.viewmodel.params.UsernameParams;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserDetailViewModel extends BaseViewModel<UsernameParams, User> {

    public void request(String username, @FetchMode int fetchMode) {
        UsernameParams params = new UsernameParams(username, fetchMode);
        UserDetailDataSource dataSource = new UserDetailDataSource();
        dataSource.setParams(params);
        request(params, dataSource);
    }
}
