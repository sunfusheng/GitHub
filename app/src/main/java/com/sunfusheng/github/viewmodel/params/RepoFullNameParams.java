package com.sunfusheng.github.viewmodel.params;

import androidx.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.http.response.ResponseData;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class RepoFullNameParams extends BaseParams {
    public String repoFullName;

    public RepoFullNameParams(String repoFullName, @FetchMode int fetchMode) {
        super(fetchMode);
        this.repoFullName = repoFullName;
    }

    @NonNull
    @Override
    public String toString() {
        return "RepoFullNameParams{" +
                "repoFullName='" + repoFullName + '\'' +
                ", fetchMode=" + ResponseData.getFetchModeString(fetchMode) +
                '}';
    }
}
