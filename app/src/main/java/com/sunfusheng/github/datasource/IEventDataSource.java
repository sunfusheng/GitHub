package com.sunfusheng.github.datasource;

import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.response.ResponseResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author sunfusheng on 2018/5/7.
 */
public interface IEventDataSource {
    Observable<ResponseResult<List<Event>>> getEvents(String username, int page, int perPage);
}
