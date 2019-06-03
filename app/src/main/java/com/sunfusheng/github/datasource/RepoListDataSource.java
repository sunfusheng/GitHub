package com.sunfusheng.github.datasource;

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

    public RepoListDataSource(String username, int page, int pageCount) {
        this.mUsername = username;
        this.mPage = page;
        this.mPageCount = pageCount;
    }

    @Override
    public Observable<ResponseData<List<Repo>>> localObservable() {
        return Observable.defer(() -> Observable.create((ObservableOnSubscribe<ResponseData<List<Repo>>>) emitter -> {
            DataSourceHelper.emitLocalResponseData(emitter, RepoDatabase.instance().getRepoDao().query(mUsername, mPageCount));
        })).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ResponseData<List<Repo>>> remoteObservable() {
        return Api.getCommonService().fetchRepoList(mUsername, mPage, mPageCount, "pushed")
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
