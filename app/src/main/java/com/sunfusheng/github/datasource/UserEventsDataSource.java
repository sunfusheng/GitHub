package com.sunfusheng.github.datasource;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.CollectionUtil;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;
import com.sunfusheng.multistate.LoadingState;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng
 * @since 2020-01-15
 */
public class UserEventsDataSource extends BaseDataSource<UsernamePageParams, List<Event>> {
    private UsernamePageParams mParams;

    @Override
    public void setParams(UsernamePageParams params) {
        this.mParams = params;
    }

    @Override
    public Observable<ResponseData<List<Event>>> localObservable() {
        return Observable.empty();
    }

    @Override
    public Observable<ResponseData<List<Event>>> remoteObservable() {
        return Api.getCommonService().fetchUserEvents(mParams.username, mParams.page, mParams.pageCount, mParams.fetchMode, Constants.Time.MINUTES_5)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .doOnNext(it -> {
                    if (DataSourceHelper.isSuccess(it) && CollectionUtil.isEmpty(it.data)) {
                        it.setLoadingState(LoadingState.EMPTY);
                    }
                });
    }
}
