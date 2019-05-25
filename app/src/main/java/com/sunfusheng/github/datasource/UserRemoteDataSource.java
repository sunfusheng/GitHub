package com.sunfusheng.github.datasource;

import com.sunfusheng.github.cache.db.UserDatabase;
import com.sunfusheng.github.datasource.base.RemoteDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserRemoteDataSource extends RemoteDataSource implements IUserDataSource {

    private static UserRemoteDataSource instance = new UserRemoteDataSource();

    private UserRemoteDataSource() {
    }

    public static UserRemoteDataSource instance() {
        return instance;
    }

    @Override
    public Observable<ResponseResult<User>> getUser(String username) {
        return Api.getCommonService().fetchUser(username)
                .subscribeOn(Schedulers.io())
                .compose(applyRemoteTransformer())
                .doOnNext(it -> {
                    if (isLoadingSuccess(it)) {
                        UserDatabase.instance().getUserDao().insert(it.data);
                    }
                });
    }
}
