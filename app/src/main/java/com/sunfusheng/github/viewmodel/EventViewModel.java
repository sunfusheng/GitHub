package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.EventLocalDataSource;
import com.sunfusheng.github.datasource.EventRemoteDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.api.ResponseResult;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class EventViewModel extends BaseViewModel {

    public final LiveData<ResponseResult<List<Event>>> liveData =
            Transformations.switchMap(requestParams, input -> getEvents(input.username, input.page, input.perPage, input.fetchMode));

    public void setRequestParams(String username, int page, @FetchMode int fetchMode) {
        setRequestParams(username, page, Constants.PER_PAGE_30, fetchMode);
    }

    public void setRequestParams(String username, int page, int perPage, @FetchMode int fetchMode) {
        requestParams.setValue(new RequestParams(username, page, perPage, fetchMode));
    }

    private LiveData<ResponseResult<List<Event>>> getEvents(String username, int page, int perPage, @FetchMode int fetchMode) {
        return fetchData(
                EventLocalDataSource.instance().getEvents(username, page, perPage),
                EventRemoteDataSource.instance().getEvents(username, page, perPage),
                fetchMode
        );
    }
}
