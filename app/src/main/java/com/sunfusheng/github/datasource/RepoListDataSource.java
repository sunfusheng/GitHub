package com.sunfusheng.github.datasource;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.cache.db.RepoDatabase;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.util.CollectionUtil;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;
import com.sunfusheng.multistate.LoadingState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class RepoListDataSource extends BaseDataSource<UsernamePageParams, List<Repo>> {
    private String mUsername;
    private int mPage;
    private int mPageCount;
    private int mFetchMode;

    @Override
    public void setParams(UsernamePageParams params) {
        this.mUsername = params.username;
        this.mPage = params.page;
        this.mPageCount = params.pageCount;
        this.mFetchMode = params.fetchMode;
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
                        } else {
                            it.setLoadingState(LoadingState.EMPTY);
                        }
                    }
                });
    }
}
