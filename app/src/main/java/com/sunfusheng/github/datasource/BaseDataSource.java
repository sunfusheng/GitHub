package com.sunfusheng.github.datasource;

import com.sunfusheng.github.net.response.ResponseData;

import io.reactivex.Observable;

/**
 * R for responses
 *
 * @author by sunfusheng on 2019-05-27
 */
abstract public class BaseDataSource<R> {

    public int localValidateTime() {
        return 0;
    }

    abstract public Observable<ResponseData<R>> localObservable();

    abstract public Observable<ResponseData<R>> remoteObservable();
}