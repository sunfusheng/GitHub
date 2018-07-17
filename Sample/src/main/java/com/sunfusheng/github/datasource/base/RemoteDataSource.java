package com.sunfusheng.github.datasource.base;

import com.sunfusheng.github.net.api.ResponseResult;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class RemoteDataSource {

    public static <T> boolean isLoadingSuccess(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.SUCCESS;
    }

    public static <T> boolean isLoadingError(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.ERROR;
    }

    public static  <T> ObservableTransformer<Response<T>, ResponseResult<T>> applyRemoteTransformer() {
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
