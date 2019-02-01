package com.sunfusheng.github.datasource;

import com.sunfusheng.github.db.RepoDatabase;
import com.sunfusheng.github.datasource.base.RemoteDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.github.util.CollectionUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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
    public Observable<ResponseResult<List<Repo>>> getRepos(String username, int page, int perPage) {
        return Api.getCommonService().fetchRepos(username, page, perPage, "pushed")
                .subscribeOn(Schedulers.io())
                .compose(applyRemoteTransformer())
                .doOnNext(it -> {
                    if (isLoadingSuccess(it)) {
                        List<Repo> data = it.data;
                        if (!CollectionUtil.isEmpty(data)) {
                            for (Repo repo : data) {
                                repo.owner_name = repo.owner.login;
                            }
                            RepoDatabase.instance().getRepoDao().insert(data);
                        }
                    }
                });
    }
}
