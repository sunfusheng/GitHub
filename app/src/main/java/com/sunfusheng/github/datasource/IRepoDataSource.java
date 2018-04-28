package com.sunfusheng.github.datasource;

import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.api.ResponseResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author sunfusheng on 2018/4/27.
 */
public interface IRepoDataSource {
    Observable<ResponseResult<List<Repo>>> getRepos(String username, int page, int perPage);
}
