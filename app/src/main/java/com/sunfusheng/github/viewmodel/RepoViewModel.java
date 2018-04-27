package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.RepoRemoteDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.api.ResponseResult;
import com.sunfusheng.github.util.NetworkUtil;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoViewModel extends ViewModel {

    private final MutableLiveData<RequestParams> params = new MutableLiveData();

    public final LiveData<ResponseResult<List<Repo>>> liveData =
            Transformations.switchMap(params, input -> getRepos(input.username, input.page, input.fetchMode));

    public void setRequestParams(String username, int page, @FetchMode int fetchMode) {
        params.setValue(new RequestParams(username, page, fetchMode));
    }

    public static class RequestParams {
        public String username;
        public int page;
        public int fetchMode;

        public RequestParams(String username, int page, int fetchMode) {
            this.username = username;
            this.page = page;
            this.fetchMode = fetchMode;
        }
    }

    private LiveData<ResponseResult<List<Repo>>> getRepos(String username, int page, @FetchMode int fetchMode) {
        MutableLiveData<ResponseResult<List<Repo>>> mutableLiveData = new MutableLiveData<>();

        if (fetchMode == FetchMode.LOCAL || !NetworkUtil.isConnected()) {

        } else if (fetchMode == FetchMode.REMOTE) {
            return ObservableLiveData.fromObservable(RepoRemoteDataSource.instance().getRepos(username, page));
        } else {

        }
        return mutableLiveData;
    }
}
