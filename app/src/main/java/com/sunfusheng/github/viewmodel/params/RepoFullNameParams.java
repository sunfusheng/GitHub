package com.sunfusheng.github.viewmodel.params;

import com.sunfusheng.github.annotation.FetchMode;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class RepoFullNameParams extends BaseParams {
    public String repoFullName;

    public RepoFullNameParams(String repoFullName, @FetchMode int fetchMode) {
        super(fetchMode);
        this.repoFullName = repoFullName;
    }
}
