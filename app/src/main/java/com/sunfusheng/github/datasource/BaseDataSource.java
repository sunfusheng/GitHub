package com.sunfusheng.github.datasource;

import com.sunfusheng.github.http.response.ResponseData;

import io.reactivex.Observable;

/**
 * R: for responses
 *
 * @author by sunfusheng on 2019-05-27
 */
abstract public class BaseDataSource<R> {

    abstract public Observable<ResponseData<R>> localObservable();

    abstract public Observable<ResponseData<R>> remoteObservable();
}