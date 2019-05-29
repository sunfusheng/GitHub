package com.sunfusheng.github.net.response;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.util.ExceptionUtil;
import com.sunfusheng.multistate.LoadingState;

import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class ResponseData<T> {
    public int code;
    public String msg;
    public T data;
    public int loadingState;
    @FetchMode
    public int fetchMode;

    public ResponseData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseData(int code, String msg, T data, int loadingState) {
        this(code, msg, data);
        this.loadingState = loadingState;
    }

    public ResponseData(Response<T> response, int loadingState) {
        this(response.code(), response.message(), response.body(), loadingState);
    }

    public String errorString() {
        return "错误码：" + code + "\n" + msg;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", loadingState=" + getLoadingStateString(loadingState) +
                ", fetchMode=" + getFetchModeString(fetchMode) +
                '}';
    }

    public static String getLoadingStateString(@LoadingState int loadingState) {
        switch (loadingState) {
            case LoadingState.LOADING:
                return "LOADING";
            case LoadingState.SUCCESS:
                return "SUCCESS";
            case LoadingState.ERROR:
                return "ERROR";
            case LoadingState.EMPTY:
                return "EMPTY";
            default:
                return "UNKNOWN";
        }
    }

    public static String getFetchModeString(@FetchMode int fetchMode) {
        switch (fetchMode) {
            case FetchMode.DEFAULT:
                return "DEFAULT";
            case FetchMode.LOCAL:
                return "LOCAL";
            case FetchMode.REMOTE:
                return "REMOTE";
            case FetchMode.FORCE_REMOTE:
                return "FORCE_REMOTE";
            default:
                return "UNKNOWN";
        }
    }

    public static <T> ResponseData<T> loading() {
        return new ResponseData<>(LoadingState.LOADING, "正在加载", null, LoadingState.LOADING);
    }

    public static <T> ResponseData<T> success(Response<T> response) {
        return new ResponseData<>(response, LoadingState.SUCCESS);
    }

    public static <T> ResponseData<T> success(T t) {
        return new ResponseData<>(LoadingState.SUCCESS, "OK", t, LoadingState.SUCCESS);
    }

    public static <T> ResponseData<T> error(ExceptionUtil.ResponseException e) {
        return new ResponseData<>(e.code, e.msg, null, LoadingState.ERROR);
    }

    public static <T> ResponseData<T> error(Throwable e) {
        return error(ExceptionUtil.handleException(e));
    }

    public static <T> ResponseData<T> error(int errorCode) {
        return error(ExceptionUtil.getResponseExceptionByErrorCode(errorCode));
    }

    public static <T> ResponseData<T> empty(int code) {
        return new ResponseData<>(code, "暂无数据", null, LoadingState.EMPTY);
    }

    public static <T> ResponseData<T> empty() {
        return empty(LoadingState.EMPTY);
    }

}
