package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.DataSourceHelper;
import com.sunfusheng.github.datasource.ReadmeDataSource;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2018/11/19
 */
public class ReadmeViewModel extends BaseViewModel {

    private final MutableLiveData<String> mParams = new MutableLiveData<>();

    public final LiveData<ResponseResult<String>> liveData = Transformations.switchMap(mParams, this::fetchReadme);

    public void request(String repoFullName) {
        mParams.setValue(repoFullName);
    }

    private LiveData<ResponseResult<String>> fetchReadme(String repoFullName) {
        MutableLiveData<ResponseResult<String>> mutableLiveData = new MutableLiveData<>();

//        String readme = ReadmeDataSource.getInstant().getReadme(repoFullName);
//        if (!TextUtils.isEmpty(readme)) {
//            mutableLiveData.setValue(ResponseResult.success(readme));
//        }

        Observable<ResponseResult<String>> fetchReadmeObservable = Api.getCommonService(FetchMode.FORCE_REMOTE)
                .fetchReadme(repoFullName)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .map(it -> {
                    ResponseResult<String> result;
                    if (it.loadingState == LoadingState.LOADING) {
                        result = ResponseResult.loading();
                    } else if (it.loadingState == LoadingState.SUCCESS) {
                        String response = it.data.string();
                        ReadmeDataSource.getInstant().cacheReadme(repoFullName, response);
                        result = ResponseResult.success(response);
                    } else if (it.loadingState == LoadingState.ERROR) {
                        result = ResponseResult.error(it.code);
                    } else {
                        result = ResponseResult.empty(it.code);
                    }
                    return result;
                });

        return ObservableLiveData.fromObservable(fetchReadmeObservable);
    }
}
