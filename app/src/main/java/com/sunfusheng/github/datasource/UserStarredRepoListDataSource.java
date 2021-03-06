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
    private UsernamePageParams mParams;

    @Override
    public void setParams(UsernamePageParams params) {
        this.mParams = params;
    }

    @Override
    public Observable<ResponseData<List<Repo>>> localObservable() {
        return Observable.empty();
    }

    @Override
    public Observable<ResponseData<List<Repo>>> remoteObservable() {
        return Api.getCommonService().fetchUserStarredRepoList(mParams.username, mParams.page, mParams.pageCount, mParams.fetchMode, Constants.Time.MINUTES_10)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .doOnNext(it -> {
                    if (DataSourceHelper.isSuccess(it) && CollectionUtil.isEmpty(it.data)) {
                        it.setLoadingState(LoadingState.EMPTY);
                    }
                });
    }
}
