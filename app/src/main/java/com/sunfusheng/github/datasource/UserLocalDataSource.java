package com.sunfusheng.github.datasource;

import com.sunfusheng.github.database.UserDatabase;
import com.sunfusheng.github.model.User;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserLocalDataSource {

    private static UserLocalDataSource instance = new UserLocalDataSource();

    private UserLocalDataSource() {
    }

    public static UserLocalDataSource instance() {
        return instance;
    }

//    public LiveData<ResponseResult<User>> getLocalUser0(String username) {
//        MutableLiveData<ResponseResult<User>> mutableLiveData = new MutableLiveData<>();
//
//        Observable.defer(() -> Observable.just(UserDatabase.instance().getUserDao().query(username)))
//                .subscribeOn(Schedulers.io())
//                .map(it -> {
//                    SystemClock.sleep(1000);
//                    return it;
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(it -> {
//                    if (it != null) {
//                        mutableLiveData.setValue(new ResponseResult<>(QueryResult.EXIST, "exist", it, LoadingState.SUCCESS));
//                    } else {
//                        mutableLiveData.setValue(new ResponseResult<>(QueryResult.NONE, username + " not exist", null, LoadingState.EMPTY));
//                    }
//                });
//
//        return mutableLiveData;
//    }

    public Observable<User> getLocalUser(String username) {
        return Observable.defer(() -> Observable.just(UserDatabase.instance().getUserDao().query(username)))
                .subscribeOn(Schedulers.io());
    }

}
