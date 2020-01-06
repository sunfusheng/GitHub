package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.datasource.RepoListDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoListViewModel extends BaseListViewModel<UsernamePageParams, List<Repo>> {
    public String username;

    @Override
    UsernamePageParams getPageParams() {
        return new UsernamePageParams(username);
    }

    @Override
    BaseDataSource<UsernamePageParams, List<Repo>> getDataSource() {
        return new RepoListDataSource();
    }
}