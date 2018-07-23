package com.sunfusheng.github.datasource;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.base.RemoteDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;

import java.util.Collections;
import java.util.List;

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
                        return Observable.just(it.data)
                                .flatMap(Observable::fromIterable)
                                .flatMap(event -> {
                                    return Api.getCommonService(fetchMode).fetchRepo(event.repo.url)
                                            .compose(applyRemoteTransformer())
                                            .map(repoResult -> {
                                                if (isLoadingSuccess(repoResult)) {
                                                    event.repo = repoResult.data;
                                                }
                                                return event;
                                            });
                                })
                                .toList()
                                .map(events -> {
                                    Collections.sort(events);
                                    it.data = events;
                                    return it;
                                })
                                .toObservable();
                    }
                    return Observable.just(it);
                });
    }

}
