package com.sunfusheng.github.datasource;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.disklrucache.ReadmeDiskLruCache;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseData;
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
    public Observable<ResponseData<String>> localObservable() {
        return Observable.defer(() -> {
            return Observable.create((ObservableOnSubscribe<ResponseData<String>>) emitter -> {
                DataSourceHelper.emitResponseData(emitter, mReadmeDiskLruCache.get(mRepoFullName));
            });
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ResponseData<String>> remoteObservable() {
        return Api.getCommonService().fetchReadme(mRepoFullName)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .map(it -> {
                    ResponseData<String> result;
                    if (it.loadingState == LoadingState.LOADING) {
                        result = ResponseData.loading();
                    } else if (it.loadingState == LoadingState.SUCCESS) {
                        String readme = it.data.string();
                        mReadmeDiskLruCache.put(mRepoFullName, readme);
                        result = ResponseData.success(readme);
                    } else if (it.loadingState == LoadingState.ERROR) {
                        result = ResponseData.error(it.code);
                    } else {
                        result = ResponseData.empty(it.code);
                    }
                    return result;
                });
    }
}
