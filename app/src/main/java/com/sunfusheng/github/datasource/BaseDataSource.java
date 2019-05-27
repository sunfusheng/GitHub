package com.sunfusheng.github.datasource;

import com.sunfusheng.github.net.response.ResponseResult;

import io.reactivex.Observable;

/**
 * @author by sunfusheng on 2019-05-27
 */
abstract public class BaseDataSource<T> {

    abstract public Observable<ResponseResult<T>> localObservable();

    abstract public Observable<ResponseResult<T>> remoteObservable();
}
