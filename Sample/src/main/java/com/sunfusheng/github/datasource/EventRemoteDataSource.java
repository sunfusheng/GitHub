package com.sunfusheng.github.datasource;

import android.util.Log;
import android.util.Pair;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.base.RemoteDataSource;
import com.sunfusheng.github.lrucache.RepoLruCache;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class EventRemoteDataSource extends RemoteDataSource {
    private static EventRemoteDataSource instance = new EventRemoteDataSource();

    private EventRemoteDataSource() {
    }

    public static EventRemoteDataSource instance() {
        return instance;
    }

    public Observable<ResponseResult<List<Event>>> getReceivedEvents(String username, int page, int perPage, @FetchMode int fetchMode) {
        return Api.getCommonService(fetchMode).fetchReceivedEvents(username, page, perPage)
                .subscribeOn(Schedulers.io())
                .compose(applyRemoteTransformer())
                .flatMap(it -> {
                    if (isLoadingSuccess(it)) {
                        Set<String> urlSet = new HashSet<>();
                        for (Event event : it.data) {
                            urlSet.add(event.repo.url);
                        }
                        return fetchData(urlSet, fetchMode)
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

    private Observable<Map<String, Repo>> fetchData(Set<String> urlSet, @FetchMode int fetchMode) {
        return Observable.defer(() -> {
            return Observable.just(urlSet)
                    .flatMap(Observable::fromIterable)
                    .flatMap(url -> {
                        if (RepoLruCache.getInstance().get(url) != null && !Constants.isReceivedEventsRefreshTimeExpired()) {
                            Log.d("------>", "复用");
                            return Observable.just(new Pair<>(url, RepoLruCache.getInstance().get(url)));
                        }
                        Log.d("------>", "不复用");
                        return Api.getCommonService(fetchMode).fetchRepo(url)
                                .compose(applyRemoteTransformer())
                                .map(repoResult -> {
                                    if (isLoadingSuccess(repoResult)) {
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
