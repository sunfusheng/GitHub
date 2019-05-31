package com.sunfusheng.github.datasource;

import android.util.Log;
import android.util.Pair;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.lrucache.RepoLruCache;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseData;

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
public class ReceivedEventsDataSource extends BaseDataSource<List<Event>> {
    private String mUsername;
    private int mPage;
    private int mPageCount;
    private int mFetchMode;

    public ReceivedEventsDataSource(String username, int page, int pageCount, @FetchMode int fetchMode) {
        this.mUsername = username;
        this.mPage = page;
        this.mPageCount = pageCount;
        this.mFetchMode = fetchMode;
    }

    @Override
    public int localCacheValidateTime() {
        return 600;
    }

    @Override
    public Observable<ResponseData<List<Event>>> localObservable() {
        return fetchReceivedEvents(FetchMode.LOCAL);
    }

    @Override
    public Observable<ResponseData<List<Event>>> remoteObservable() {
        return fetchReceivedEvents(mFetchMode);
    }

    private Observable<ResponseData<List<Event>>> fetchReceivedEvents(@FetchMode int fetchMode) {
        if (fetchMode == FetchMode.DEFAULT) {
            fetchMode = FetchMode.REMOTE;
        }
        int finalFetchMode = fetchMode;
        Log.d("sfs", "fetchReceivedEvents() fetchMode: " + ResponseData.getFetchModeString(finalFetchMode));

        return Api.getCommonService().fetchReceivedEvents(mUsername, mPage, mPageCount, fetchMode)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .flatMap(it -> {
                    if (DataSourceHelper.isSuccess(it)) {
                        Set<String> urlSet = new HashSet<>();
                        for (Event event : it.data) {
                            urlSet.add(event.repo.url);
                        }
                        return fetchRepoDetail(urlSet, finalFetchMode)
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
                        if (RepoLruCache.getInstance().get(url) != null && !Constants.isReceivedEventsRefreshTimeExpired()) {
                            return Observable.just(new Pair<>(url, RepoLruCache.getInstance().get(url)));
                        }
                        return Api.getCommonService().fetchRepoDetail(url)
                                .compose(DataSourceHelper.applyRemoteTransformer())
                                .map(repoResult -> {
                                    if (DataSourceHelper.isSuccess(repoResult)) {
                                        RepoLruCache.getInstance().put(url, repoResult.data);
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
