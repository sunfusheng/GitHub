package com.sunfusheng.github.datasource;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.util.CollectionUtil;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;
import com.sunfusheng.multistate.LoadingState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng
 * @since 2020-01-12
 */
public class UserStarredRepoListDataSource extends BaseDataSource<UsernamePageParams, List<Repo>> {
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
        return Observable.empty();
    }

    @Override
    public Observable<ResponseData<List<Repo>>> remoteObservable() {
        return Api.getCommonService().fetchUserStarredRepoList(mUsername, mPage, mPageCount, mFetchMode, Constants.Time.MINUTES_10)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .doOnNext(it -> {
                    if (DataSourceHelper.isSuccess(it)) {
                        List<Repo> data = it.data;
                        if (CollectionUtil.isEmpty(data)) {
                            it.setLoadingState(LoadingState.EMPTY);
                        }
                    }
                });
    }
}
