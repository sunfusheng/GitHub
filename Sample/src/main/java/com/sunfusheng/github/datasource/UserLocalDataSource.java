package com.sunfusheng.github.datasource;

import com.sunfusheng.github.database.UserDatabase;
import com.sunfusheng.github.datasource.base.LocalDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.response.ResponseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserLocalDataSource extends LocalDataSource implements IUserDataSource {

    private static UserLocalDataSource instance = new UserLocalDataSource();

    private UserLocalDataSource() {
    }

    public static UserLocalDataSource instance() {
        return instance;
    }

    @Override
    public Observable<ResponseResult<User>> getUser(String username) {
        return Observable.defer(() -> Observable.create((ObservableOnSubscribe<ResponseResult<User>>) emitter -> {
            emitResult(emitter, UserDatabase.instance().getUserDao().query(username));
        })).subscribeOn(Schedulers.io());
    }
}
