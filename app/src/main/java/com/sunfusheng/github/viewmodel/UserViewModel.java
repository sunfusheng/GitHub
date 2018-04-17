package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.SystemClock;
import android.util.Log;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.annotation.QueryResult;
import com.sunfusheng.github.datasource.UserLocalDataSource;
import com.sunfusheng.github.datasource.UserRemoteDataSource;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.net.CommonObserver;
import com.sunfusheng.github.net.ObservableLiveData;
import com.sunfusheng.github.net.ObservableResponseLiveData;
import com.sunfusheng.github.net.ResponseResult;
import com.sunfusheng.github.util.ExceptionUtil;
import com.sunfusheng.github.util.NetworkUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserViewModel extends ViewModel {

//    public LiveData<ResponseResult<User>> getUser(String username) {
//        LiveData<ResponseResult<User>> localLiveData = UserLocalDataSource.instance().getLocalUser(username);
//        LiveData<ResponseResult<User>> remoteLiveData = UserRemoteDataSource.instance().getRemoteUser("Blankj");
//        return localLiveData;
//    }

//    public LiveData<ResponseResult<User>> getUser(String username, @FetchMode int fetchMode) {
//        if (fetchMode == FetchMode.LOCAL || !NetworkUtil.isConnected()) {
//            return UserLocalDataSource.instance().getLocalUser(username);
//        } else if (fetchMode == FetchMode.REMOTE) {
//            return UserRemoteDataSource.instance().getRemoteUser(username);
//        } else {
//
//            MutableLiveData<ResponseResult<User>> mutableLiveData = new MutableLiveData<>();
//
//            LiveData<ResponseResult<User>> localLiveData = UserLocalDataSource.instance().getLocalUser(username);
//            LiveData<ResponseResult<User>> remoteLiveData = UserRemoteDataSource.instance().getRemoteUser("Blankj");
//
//            Observable<LiveData<ResponseResult<User>>> localObservable = Observable.defer(() -> Observable.just(localLiveData))
//                    .subscribeOn(Schedulers.io());
//
//            Observable<LiveData<ResponseResult<User>>> remoteObservable = Observable.just(remoteLiveData)
//                    .subscribeOn(Schedulers.io());
//
//            localObservable
//                    .publish(local -> Observable.merge(local, remoteObservable).takeUntil(remoteObservable))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(it -> mutableLiveData.setValue(it.getValue()));
//            return mutableLiveData;
//        }
//    }

    public LiveData<ResponseResult<User>> getUser(String username, @FetchMode int fetchMode) {
        if (fetchMode == FetchMode.LOCAL || !NetworkUtil.isConnected()) {
            return ObservableLiveData.fromObservable(UserLocalDataSource.instance().getLocalUser(username));
        } else if (fetchMode == FetchMode.REMOTE) {
            return ObservableResponseLiveData.fromObservable(UserRemoteDataSource.instance().getRemoteUser(username));
        } else {
            MutableLiveData<ResponseResult<User>> mutableLiveData = new MutableLiveData<>();

            Observable<ResponseResult<User>> localObservable = UserLocalDataSource.instance().getLocalUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(it -> {
                        SystemClock.sleep(1000);
                        if (it != null) {
                            return new ResponseResult<>(QueryResult.EXIST, "exist", it, LoadingState.SUCCESS);
                        }
                        return new ResponseResult<>(QueryResult.NONE, username + "not exist", null, LoadingState.EMPTY);
                    });

            Observable<ResponseResult<User>> remoteObservable = UserRemoteDataSource.instance().getRemoteUser("Blankj")
                    .map(it -> {
                        SystemClock.sleep(2000);
                        if (it == null) {
                            return ResponseResult.empty();
                        }
                        if (it.isSuccessful()) {
                            return ResponseResult.success(it);
                        }
                        return ResponseResult.error(ExceptionUtil.getResponseExceptionByErrorCode(it.code()));

                    });

            Observable.concat(localObservable, remoteObservable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<User>() {
                        @Override
                        public void onNotify(ResponseResult<User> result) {
                            mutableLiveData.postValue(result);
                            Log.d("--->", "1.onNotify() :"+result);
                        }
                    });

            return mutableLiveData;
        }
    }
}
