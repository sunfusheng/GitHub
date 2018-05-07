package com.sunfusheng.github.datasource;

import com.sunfusheng.github.database.EventDatabase;
import com.sunfusheng.github.datasource.base.RemoteDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.api.Api;
import com.sunfusheng.github.net.api.ResponseResult;
import com.sunfusheng.github.util.CollectionUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class EventRemoteDataSource extends RemoteDataSource implements IEventDataSource {

    private static EventRemoteDataSource instance = new EventRemoteDataSource();

    private EventRemoteDataSource() {
    }

    public static EventRemoteDataSource instance() {
        return instance;
    }

    @Override
    public Observable<ResponseResult<List<Event>>> getEvents(String username, int page, int perPage) {
        return Api.getCommonService().fetchEvents(username, page, perPage)
                .subscribeOn(Schedulers.io())
                .compose(applyRemoteTransformer())
                .doOnNext(it -> {
                    if (isLoadingSuccess(it)) {
                        List<Event> data = it.data;
                        if (!CollectionUtil.isEmpty(data)) {
                            for (Event event : data) {
                                if (event.payload != null && !CollectionUtil.isEmpty(event.payload.commits)) {
                                    event.payload.commit = event.payload.commits.get(0);
                                }
                            }
                            EventDatabase.instance().getEventDao().insert(data);
                        }
                    }
                });
    }

}
