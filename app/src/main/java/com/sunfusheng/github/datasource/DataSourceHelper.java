package com.sunfusheng.github.datasource;

import com.sunfusheng.github.net.response.ResponseData;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class DataSourceHelper {

    public static <T> void emitResult(ObservableEmitter<ResponseData<T>> emitter, T t) {
        if (t != null) {
            emitter.onNext(ResponseData.success(t));
        }
        emitter.onComplete();
    }

    public static <T> boolean isLoading(ResponseData<T> result) {
        return result != null && result.loadingState == LoadingState.LOADING;
    }

    public static <T> boolean isSuccess(ResponseData<T> result) {
        return result != null && result.loadingState == LoadingState.SUCCESS;
    }

    public static <T> boolean isError(ResponseData<T> result) {
        return result != null && result.loadingState == LoadingState.ERROR;
    }

    public static <T> boolean isEmpty(ResponseData<T> result) {
        return result != null && result.loadingState == LoadingState.EMPTY;
    }

    public static <T> ObservableTransformer<T, ResponseData<T>> applyLocalTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .map(it -> {
                    if (it == null) {
                        return ResponseData.empty();
                    }
                    return ResponseData.success(it);
                });
    }

    public static <T> ObservableTransformer<Response<T>, ResponseData<T>> applyRemoteTransformer() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .map(it -> {
                    if (it == null) {
                        return ResponseData.empty();
                    }

                    if (it.isSuccessful()) {
                        if (it.body() == null) {
                            return ResponseData.empty(it.code());
                        }
                        return ResponseData.success(it);
                    } else {
                        return ResponseData.error(it.code());
                    }
                });
    }
}
