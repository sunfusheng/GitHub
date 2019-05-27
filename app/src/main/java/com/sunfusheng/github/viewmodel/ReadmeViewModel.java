package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.ReadmeDataSource;
import com.sunfusheng.github.net.response.ResponseResult;

/**
 * @author by sunfusheng on 2018/11/19
 */
public class ReadmeViewModel extends BaseViewModel {
    private final MutableLiveData<Param> mParams = new MutableLiveData<>();
    public final LiveData<ResponseResult<String>> liveData = Transformations.switchMap(mParams, param -> fetchReadme(param.repoFullName, param.fetchMode));

    static class Param {
        private String repoFullName;
        private int fetchMode;

        Param(String repoFullName, @FetchMode int fetchMode) {
            this.repoFullName = repoFullName;
            this.fetchMode = fetchMode;
        }
    }

    public void request(String repoFullName, @FetchMode int fetchMode) {
        mParams.setValue(new Param(repoFullName, fetchMode));
    }

    private LiveData<ResponseResult<String>> fetchReadme(String repoFullName, @FetchMode int fetchMode) {
        ReadmeDataSource dataSource = new ReadmeDataSource(repoFullName, fetchMode);
        return fetchData(
                dataSource.localObservable(),
                dataSource.remoteObservable(),
                fetchMode
        );
    }
}
