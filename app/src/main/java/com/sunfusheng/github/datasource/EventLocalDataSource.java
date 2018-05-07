package com.sunfusheng.github.datasource;

import com.sunfusheng.github.database.EventDatabase;
import com.sunfusheng.github.datasource.base.LocalDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.api.ResponseResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class EventLocalDataSource extends LocalDataSource implements IEventDataSource {

    private static EventLocalDataSource instance = new EventLocalDataSource();

    private EventLocalDataSource() {
    }

    public static EventLocalDataSource instance() {
        return instance;
    }

    @Override
    public Observable<ResponseResult<List<Event>>> getEvents(String username, int page, int perPage) {
        return Observable.defer(() -> Observable.create((ObservableOnSubscribe<ResponseResult<List<Event>>>) emitter -> {
            emitResult(emitter, EventDatabase.instance().getEventDao().query(username, perPage));
        })).subscribeOn(Schedulers.io());
    }
}
