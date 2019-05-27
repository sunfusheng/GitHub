package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.lrucache.RepoLruCache;
import com.sunfusheng.github.datasource.ReceivedEventsDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.response.ResponseResult;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class ReceivedEventsViewModel extends BaseViewModel {

    public final LiveData<ResponseResult<List<Event>>> liveData =
            Transformations.switchMap(mParams, params -> fetchReceivedEvents(params.username, params.page, params.pageCount, params.fetchMode));

    public void request(String username, int page, int pageCount, @FetchMode int fetchMode) {
        mParams.setValue(new RequestParams(username, page, pageCount, fetchMode));
    }

    private LiveData<ResponseResult<List<Event>>> fetchReceivedEvents(String username, int page, int pageCount, @FetchMode int fetchMode) {
        ReceivedEventsDataSource dataSource = new ReceivedEventsDataSource(username, page, pageCount, fetchMode);
        return fetchData(
                dataSource.localObservable(),
                dataSource.remoteObservable(),
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
