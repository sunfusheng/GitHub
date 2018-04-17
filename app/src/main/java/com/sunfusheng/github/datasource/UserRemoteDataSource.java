package com.sunfusheng.github.datasource;

import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.Api;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserRemoteDataSource {

    private static UserRemoteDataSource instance = new UserRemoteDataSource();

    private UserRemoteDataSource() {
    }

    public static UserRemoteDataSource instance() {
        return instance;
    }

//    public LiveData<ResponseResult<User>> getRemoteUser0(String username) {
//        return ObservableResponseLiveData.fromObservable(
//                Api.getAuthService().fetchUser(username)
//                        .subscribeOn(Schedulers.io())
//                        .map(it -> {
//                            SystemClock.sleep(3000);
//                            return it;
//                        }).observeOn(AndroidSchedulers.mainThread())
//        );
//    }

    public Observable<Response<User>> getRemoteUser(String username) {
        return Api.getAuthService().fetchUser(username)
                        .subscribeOn(Schedulers.io());
    }
}
