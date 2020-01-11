package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.datasource.UserListDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng
 * @since 2020-01-11
 */
public class FollowersViewModel extends BaseListViewModel<UsernamePageParams, User> {
    @Override
    UsernamePageParams getPageParams() {
        return new UsernamePageParams(username);
    }

    @Override
    BaseDataSource<UsernamePageParams, List<User>> getDataSource() {
        return new UserListDataSource(UserListDataSource.FROM_FOLLOWER);
    }
}
