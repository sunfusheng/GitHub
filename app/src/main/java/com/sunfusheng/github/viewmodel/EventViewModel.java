package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.EventRemoteDataSource;
import com.sunfusheng.github.datasource.lrucache.RepoLruCache;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.response.ResponseResult;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class EventViewModel extends BaseViewModel {

    public final LiveData<ResponseResult<List<Event>>> liveData =
            Transformations.switchMap(requestParams, input -> getReceivedEvents(input.username, input.page, input.perPage, input.fetchMode));

    public void setRequestParams(String username, int page, @FetchMode int fetchMode) {
        setRequestParams(username, page, Constants.PER_PAGE_30, fetchMode);
    }

    public void setRequestParams(String username, int page, int perPage, @FetchMode int fetchMode) {
        requestParams.setValue(new RequestParams(username, page, perPage, fetchMode));
    }

    private LiveData<ResponseResult<List<Event>>> getReceivedEvents(String username, int page, int perPage, @FetchMode int fetchMode) {
        return fetchData(
                EventRemoteDataSource.instance().getReceivedEvents(username, page, perPage, FetchMode.LOCAL),
                EventRemoteDataSource.instance().getReceivedEvents(username, page, perPage, FetchMode.REMOTE),
                fetchMode
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (Constants.isReceivedEventsRefreshTimeExpired()) {
            RepoLruCache.getInstance().clear();
        }
    }
}
