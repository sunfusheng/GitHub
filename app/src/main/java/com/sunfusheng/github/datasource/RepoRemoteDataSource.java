package com.sunfusheng.github.datasource;

import com.sunfusheng.github.database.RepoDatabase;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.api.Api;
import com.sunfusheng.github.net.api.ResponseResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoRemoteDataSource extends RemoteDataSource implements IRepoDataSource {

    private static RepoRemoteDataSource instance = new RepoRemoteDataSource();

    private RepoRemoteDataSource() {
    }

    public static RepoRemoteDataSource instance() {
        return instance;
    }

    @Override
    public Observable<ResponseResult<List<Repo>>> getRepos(String username, int page) {
        return Api.getCommonService().fetchRepos(username, "pushed", page)
                .compose(applyRemoteTransformer())
                .doOnNext(it -> {
                    if (isLoadingSuccess(it)) {
                        RepoDatabase.instance().getRepoDao().insert(it.data);
                    }
                });
    }
}
