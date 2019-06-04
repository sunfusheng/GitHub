package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.RepoListDataSource;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.viewmodel.params.PageParams;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoListViewModel extends BaseViewModel<UsernamePageParams, List<Repo>> {

    public void doRequest(String username, int page, @FetchMode int fetchMode) {
        request(new UsernamePageParams(username, page, PageParams.PAGE_COUNT, fetchMode),
                new RepoListDataSource(username, page, PageParams.PAGE_COUNT, fetchMode));
    }
}
