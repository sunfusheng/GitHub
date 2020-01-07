package com.sunfusheng.github.datasource;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.cache.db.AccessTimeDatabase;
import com.sunfusheng.github.http.interceptors.CommonInterceptor;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.model.AccessTime;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Response;

/**
 * @author by sunfusheng on 2019-05-27
 */
public class DataSourceHelper {

    public static <T> void emitLocalResponseData(ObservableEmitter<ResponseData<T>> emitter, T t) {
        if (t != null) {
            ResponseData<T> localResponseData = ResponseData.success(t);
            localResponseData.setFetchMode(FetchMode.LOCAL);
            emitter.onNext(localResponseData);
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

                    Request request = it.raw().request();
                    @FetchMode int realFetchMode = FetchMode.REMOTE;
                    String realFetchModeString = request.header("real-fetch-mode");
                    if (realFetchModeString != null) {
                        realFetchMode = ResponseData.getFetchMode(realFetchModeString);
                    }

                    ResponseData<T> responseData;
                    if (it.isSuccessful()) {
                        if (it.body() == null) {
                            responseData = ResponseData.empty(it.code());
                        } else {
                            responseData = ResponseData.success(it);
                        }
                    } else {
                        responseData = ResponseData.error(it.code());
                    }
                    responseData.setFetchMode(realFetchMode);
                    responseData.url = request.url().toString();
                    responseData.localCacheValidateTime = CommonInterceptor.getLocalCacheValidateTimeByRequestHeader(request);
                    AccessTime accessTime = AccessTimeDatabase.instance().getAccessTimeDao().query(request.url().toString());
                    responseData.lastAccessTime = accessTime != null ? accessTime.lastAccessTime : 0;
                    return responseData;
                });
    }
}
