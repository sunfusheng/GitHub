package com.sunfusheng.github.datasource;

import android.util.Pair;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class ReceivedEventsDataSource extends BaseDataSource<UsernamePageParams, List<Event>> {
    private UsernamePageParams mParams;

    @Override
    public void setParams(UsernamePageParams params) {
        this.mParams = params;
    }

    @Override
    public Observable<ResponseData<List<Event>>> localObservable() {
        return fetchReceivedEvents(FetchMode.LOCAL);
    }

    @Override
    public Observable<ResponseData<List<Event>>> remoteObservable() {
        return fetchReceivedEvents(mParams.fetchMode);
    }

    private Observable<ResponseData<List<Event>>> fetchReceivedEvents(@FetchMode int fetchMode) {
        return Api.getCommonService().fetchReceivedEvents(mParams.username, mParams.page, mParams.pageCount, fetchMode, Constants.Time.MINUTES_10)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .flatMap(it -> {
                    if (DataSourceHelper.isSuccess(it)) {
                        Set<String> urlSet = new HashSet<>();
                        for (Event event : it.data) {
                            urlSet.add(event.repo.url);
                        }
                        return fetchRepoDetail(urlSet, fetchMode)
                                .map(map -> {
                                    for (Event event : it.data) {
                                        event.repo = map.get(event.repo.url);
                                    }
                                    return it;
                                });
                    }
                    return Observable.just(it);
                });
    }

    private Observable<Map<String, Repo>> fetchRepoDetail(Set<String> urlSet, @FetchMode int fetchMode) {
        return Observable.defer(() -> {
            return Observable.just(urlSet)
                    .flatMap(Observable::fromIterable)
                    .flatMap(url -> {
                        return Api.getCommonService().fetchRepoDetail(url, fetchMode, Constants.Time.MINUTES_10)
                                .compose(DataSourceHelper.applyRemoteTransformer())
                                .map(repoResult -> {
                                    if (DataSourceHelper.isSuccess(repoResult)) {
                                        return new Pair<>(url, repoResult.data);
                                    }
                                    return new Pair<>(url, new Repo());
                                });
                    })
                    .toList()
                    .map(pairs -> {
                        Map<String, Repo> map = new HashMap<>();
                        for (Pair<String, Repo> pair : pairs) {
                            map.put(pair.first, pair.second);
                        }
                        return map;
                    })
                    .toObservable();
        });
    }
}
