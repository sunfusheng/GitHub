package com.sunfusheng.github.datasource;

import com.sunfusheng.github.http.response.ResponseData;

import io.reactivex.Observable;

/**
 * P: for parameters
 * R: for responses
 *
 * @author by sunfusheng on 2019-05-27
 */
abstract public class BaseDataSource<P, R> {
    abstract public void setParams(P params);

    abstract public Observable<ResponseData<R>> localObservable();

    abstract public Observable<ResponseData<R>> remoteObservable();
}