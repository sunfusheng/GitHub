package com.sunfusheng.github.datasource;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.db.RepoDatabase;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseData;
import com.sunfusheng.github.util.CollectionUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class RepoListDataSource extends BaseDataSource<List<Repo>> {
    private String mUsername;
    private int mPage;
    private int mPageCount;
    private int mFetchMode;

    public RepoListDataSource(String username, int page, int pageCount, @FetchMode int fetchMode) {
        this.mUsername = username;
        this.mPage = page;
        this.mPageCount = pageCount;
        this.mFetchMode = fetchMode;
    }

    @Override
    public Observable<ResponseData<List<Repo>>> localObservable() {
        return Observable.defer(() -> Observable.create((ObservableOnSubscribe<ResponseData<List<Repo>>>) emitter -> {
            List<Repo> repoList = RepoDatabase.instance().getRepoDao().query(mUsername, mPageCount);
            DataSourceHelper.emitLocalResponseData(emitter, CollectionUtil.isEmpty(repoList) ? null : repoList);
        })).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ResponseData<List<Repo>>> remoteObservable() {
        return Api.getCommonService().fetchRepoList(mUsername, mPage, mPageCount, "pushed", mFetchMode, Constants.Time.MINUTES_10)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .doOnNext(it -> {
                    if (DataSourceHelper.isSuccess(it)) {
                        List<Repo> data = it.data;
                        if (!CollectionUtil.isEmpty(data)) {
                            for (Repo repo : data) {
                                repo.owner_name = repo.owner.login;
                            }
                            RepoDatabase.instance().getRepoDao().insert(data);
                        }
                    }
                });
    }
}
