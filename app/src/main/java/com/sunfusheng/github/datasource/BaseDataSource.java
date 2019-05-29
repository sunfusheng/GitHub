package com.sunfusheng.github.datasource;

import com.sunfusheng.github.net.response.ResponseData;

import io.reactivex.Observable;

/**
 * R: for responses
 *
 * @author by sunfusheng on 2019-05-27
 */
abstract public class BaseDataSource<R> {

    /**
     * @return 本地缓存的有效时间，单位：秒
     */
    public int localCacheValidateTime() {
        return 0;
    }

    abstract public Observable<ResponseData<R>> localObservable();

    abstract public Observable<ResponseData<R>> remoteObservable();
}