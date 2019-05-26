package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.sunfusheng.github.datasource.ReadmeDataSource;
import com.sunfusheng.github.datasource.base.RemoteDataSource;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2018/11/19
 */
public class ReadmeViewModel extends ViewModel {

    private final MutableLiveData<String> params = new MutableLiveData<>();

    public final LiveData<ResponseResult<String>> liveData =
            Transformations.switchMap(params, this::fetchReadme);

    public void setRequestParams(String repoFullName) {
        params.setValue(repoFullName);
    }

    private Disposable mDisposable;

    private LiveData<ResponseResult<String>> fetchReadme(String repoFullName) {
        MutableLiveData<ResponseResult<String>> mutableLiveData = new MutableLiveData<>();

        String readme = ReadmeDataSource.getInstant().getReadme(repoFullName);
        if (!TextUtils.isEmpty(readme)) {
            mutableLiveData.setValue(ResponseResult.success(readme));
        }

        mDisposable = Api.getCommonService().fetchReadme(repoFullName)
                .subscribeOn(Schedulers.io())
                .compose(RemoteDataSource.applyRemoteTransformer())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    if (it.loadingState == LoadingState.LOADING) {
                        mutableLiveData.setValue(ResponseResult.loading());
                    } else if (it.loadingState == LoadingState.SUCCESS) {
                        String response = it.data.string();
                        ReadmeDataSource.getInstant().cacheReadme(repoFullName, response);
                        mutableLiveData.setValue(ResponseResult.success(response));
                    } else if (it.loadingState == LoadingState.ERROR) {
                        mutableLiveData.setValue(ResponseResult.error(it.code));
                    } else if (it.loadingState == LoadingState.EMPTY) {
                        mutableLiveData.setValue(ResponseResult.empty(it.code));
                    }
                }, Throwable::printStackTrace);
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
        super.onCleared();
    }
}
