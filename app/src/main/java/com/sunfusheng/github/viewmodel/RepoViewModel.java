package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.RepoRemoteDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.api.ResponseResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoViewModel extends BaseViewModel {


    public final LiveData<ResponseResult<List<Repo>>> liveData =
            Transformations.switchMap(requestParams, input -> getRepos(input.username, input.page, input.fetchMode));

    public void setRequestParams(String username, int page, @FetchMode int fetchMode) {
        setRequestParams(username, page, Constants.PER_PAGE_30, fetchMode);
    }

    public void setRequestParams(String username, int page, int perPage, @FetchMode int fetchMode) {
        requestParams.setValue(new RequestParams(username, page, perPage, fetchMode));
    }

    private LiveData<ResponseResult<List<Repo>>> getRepos(String username, int page, @FetchMode int fetchMode) {
        return fetchData(Observable.empty(),
                RepoRemoteDataSource.instance().getRepos(username, page),
                fetchMode);
    }
}
