package com.sunfusheng.github.datasource;

import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.net.ResponseResult;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class RemoteDataSource {

    public <T> boolean isSuccess(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.SUCCESS;
    }

    public <T> ObservableTransformer<Response<T>, ResponseResult<T>> applyRemoteTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .map(it -> {
                    if (it == null) {
                        return ResponseResult.empty();
                    }

                    if (it.isSuccessful()) {
                        if (it.body() == null) {
                            return ResponseResult.empty();
                        }
                        return ResponseResult.success(it);
                    } else {
                        return ResponseResult.error(it.code());
                    }
                });
    }
}
