package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.RepoListDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.response.ResponseResult;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoListViewModel extends BaseViewModel {

    public final LiveData<ResponseResult<List<Repo>>> liveData =
            Transformations.switchMap(mParams, params -> fetchRepoList(params.username, params.page, params.pageCount, params.fetchMode));

    public void request(String username, int page, @FetchMode int fetchMode) {
        request(username, page, Constants.PAGE_COUNT, fetchMode);
    }

    public void request(String username, int page, int perPage, @FetchMode int fetchMode) {
        mParams.setValue(new RequestParams(username, page, perPage, fetchMode));
    }

    private LiveData<ResponseResult<List<Repo>>> fetchRepoList(String username, int page, int pageCount, @FetchMode int fetchMode) {
        RepoListDataSource dataSource = new RepoListDataSource(username, page, pageCount);
        return fetchData(
                dataSource.localObservable(),
                dataSource.remoteObservable(),
                fetchMode
        );
    }
}
