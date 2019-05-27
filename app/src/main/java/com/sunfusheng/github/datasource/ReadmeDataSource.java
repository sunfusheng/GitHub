package com.sunfusheng.github.datasource;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.disklrucache.ReadmeDiskLruCache;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019-05-26
 */
public class ReadmeDataSource extends BaseDataSource<String> {
    private String mRepoFullName;
    private int mFetchMode;
    private ReadmeDiskLruCache mReadmeDiskLruCache = new ReadmeDiskLruCache();

    public ReadmeDataSource(String repoFullName, @FetchMode int fetchMode) {
        this.mRepoFullName = repoFullName;
        this.mFetchMode = fetchMode;
    }

    @Override
    public Observable<ResponseResult<String>> localObservable() {
        return Observable.defer(() -> {
            return Observable.create((ObservableOnSubscribe<ResponseResult<String>>) emitter -> {
                DataSourceHelper.emitResult(emitter, mReadmeDiskLruCache.get(mRepoFullName));
            });
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ResponseResult<String>> remoteObservable() {
        return Api.getCommonService(mFetchMode)
                .fetchReadme(mRepoFullName)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .map(it -> {
                    ResponseResult<String> result;
                    if (it.loadingState == LoadingState.LOADING) {
                        result = ResponseResult.loading();
                    } else if (it.loadingState == LoadingState.SUCCESS) {
                        String readme = it.data.string();
                        mReadmeDiskLruCache.put(mRepoFullName, readme);
                        result = ResponseResult.success(readme);
                    } else if (it.loadingState == LoadingState.ERROR) {
                        result = ResponseResult.error(it.code);
                    } else {
                        result = ResponseResult.empty(it.code);
                    }
                    return result;
                });
    }
}
