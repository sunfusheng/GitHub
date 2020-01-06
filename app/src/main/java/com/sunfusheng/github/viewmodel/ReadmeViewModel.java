package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.ReadmeDataSource;
import com.sunfusheng.github.viewmodel.params.RepoFullNameParams;

/**
 * @author by sunfusheng on 2018/11/19
 */
public class ReadmeViewModel extends BaseViewModel<RepoFullNameParams, String> {

    public void request(String repoFullName, @FetchMode int fetchMode) {
        RepoFullNameParams params = new RepoFullNameParams(repoFullName, fetchMode);
        ReadmeDataSource dataSource = new ReadmeDataSource();
        dataSource.setParams(params);
        request(params, dataSource);
    }
}