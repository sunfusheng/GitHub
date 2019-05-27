package com.sunfusheng.github.datasource;

import com.sunfusheng.github.cache.db.UserDatabase;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class UserDetailDataSource extends BaseDataSource<User> {
    private String mUserName;

    public UserDetailDataSource(String userName) {
        this.mUserName = userName;
    }

    @Override
    public Observable<ResponseResult<User>> localObservable() {
        return Observable.defer(() -> Observable.create((ObservableOnSubscribe<ResponseResult<User>>) emitter -> {
            DataSourceHelper.emitResult(emitter, UserDatabase.instance().getUserDao().query(mUserName));
        })).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ResponseResult<User>> remoteObservable() {
        return Api.getCommonService().fetchUserDetail(mUserName)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .doOnNext(it -> {
                    if (DataSourceHelper.isSuccess(it)) {
                        UserDatabase.instance().getUserDao().insert(it.data);
                    }
                });
    }
}
