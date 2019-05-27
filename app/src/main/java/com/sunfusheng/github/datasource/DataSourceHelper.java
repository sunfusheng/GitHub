package com.sunfusheng.github.datasource;

import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class DataSourceHelper {

    public static <T> void emitResult(ObservableEmitter<ResponseResult<T>> emitter, T t) {
        if (t != null) {
            emitter.onNext(ResponseResult.success(t));
        }
        emitter.onComplete();
    }

    public static <T> boolean isLoading(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.LOADING;
    }

    public static <T> boolean isSuccess(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.SUCCESS;
    }

    public static <T> boolean isError(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.ERROR;
    }

    public static <T> boolean isEmpty(ResponseResult<T> result) {
        return result != null && result.loadingState == LoadingState.EMPTY;
    }

    public static <T> ObservableTransformer<T, ResponseResult<T>> applyLocalTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .map(it -> {
                    if (it == null) {
                        return ResponseResult.empty();
                    }
                    return ResponseResult.success(it);
                });
    }

    public static <T> ObservableTransformer<Response<T>, ResponseResult<T>> applyRemoteTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .map(it -> {
                    if (it == null) {
                        return ResponseResult.empty();
                    }

                    if (it.isSuccessful()) {
                        if (it.body() == null) {
                            return ResponseResult.empty(it.code());
                        }
                        return ResponseResult.success(it);
                    } else {
                        return ResponseResult.error(it.code());
                    }
                });
    }
}
