package com.sunfusheng.github.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.annotation.QueryResult;
import com.sunfusheng.github.database.UserDatabase;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.ResponseResult;
import com.sunfusheng.github.util.ExceptionUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserLocalDataSource implements IUserDataSource {

    private static UserLocalDataSource instance = new UserLocalDataSource();

    private UserLocalDataSource() {
    }

    public static UserLocalDataSource instance() {
        return instance;
    }

    @Override
    public LiveData<ResponseResult<User>> getUser(String username) {
        MutableLiveData<ResponseResult<User>> mutableLiveData = new MutableLiveData<>();
        Observable.defer(() -> Observable.just(mutableLiveData))
                .subscribeOn(Schedulers.io())
                .map((Function<MutableLiveData<ResponseResult<User>>, ResponseResult<User>>) responseResultMutableLiveData -> {
                    User user = UserDatabase.instance().getUserDao().query(username);
                    if (user != null) {
                        return new ResponseResult<>(QueryResult.EXIST, "OK", user, LoadingState.SUCCESS);
                    } else {
                        return new ResponseResult<>(QueryResult.NONE, username + " not exist", null, LoadingState.EMPTY);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseResult<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mutableLiveData.setValue(ResponseResult.loading());
                    }

                    @Override
                    public void onNext(ResponseResult<User> result) {
                        mutableLiveData.setValue(result);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mutableLiveData.setValue(ResponseResult.error(ExceptionUtil.handleException(throwable)));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mutableLiveData;
    }
}
