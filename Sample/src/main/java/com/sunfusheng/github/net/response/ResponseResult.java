package com.sunfusheng.github.net.response;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.util.ExceptionUtil;
import com.sunfusheng.multistate.LoadingState;

import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class ResponseResult<T> {
    public int code;
    public String msg;
    public T data;
    public int loadingState;
    @FetchMode
    public int fetchMode;

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(int code, String msg, T data, int loadingState) {
        this(code, msg, data);
        this.loadingState = loadingState;
    }

    public ResponseResult(Response<T> response, int loadingState) {
        this(response.code(), response.message(), response.body(), loadingState);
    }

    public String errorString() {
        return "错误码：" + code + "\n" + msg;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", loadingState=" + getLoadingStateString(loadingState) +
                ", fetchMode=" + getFetchModeString(fetchMode) +
                '}';
    }

    public static String getLoadingStateString(@LoadingState int loadingState) {
        switch (loadingState) {
            case LoadingState.SUCCESS:
                return "SUCCESS";
            case LoadingState.ERROR:
                return "ERROR";
            case LoadingState.EMPTY:
                return "EMPTY";
            default:
            case LoadingState.LOADING:
                return "LOADING";
        }
    }

    public static String getFetchModeString(@FetchMode int fetchMode) {
        switch (fetchMode) {
            case FetchMode.LOCAL:
                return "LOCAL";
            case FetchMode.REMOTE:
                return "REMOTE";
            default:
            case FetchMode.DEFAULT:
                return "DEFAULT";
        }
    }

    public static <T> ResponseResult<T> loading() {
        return new ResponseResult<>(LoadingState.LOADING, "正在加载", null, LoadingState.LOADING);
    }

    public static <T> ResponseResult<T> success(Response<T> response) {
        return new ResponseResult<>(response, LoadingState.SUCCESS);
    }

    public static <T> ResponseResult<T> success(T t) {
        return new ResponseResult<>(LoadingState.SUCCESS, "OK", t, LoadingState.SUCCESS);
    }

    public static <T> ResponseResult<T> error(ExceptionUtil.ResponseException e) {
        return new ResponseResult<>(e.code, e.msg, null, LoadingState.ERROR);
    }

    public static <T> ResponseResult<T> error(Throwable e) {
        return error(ExceptionUtil.handleException(e));
    }

    public static <T> ResponseResult<T> error(int errorCode) {
        return error(ExceptionUtil.getResponseExceptionByErrorCode(errorCode));
    }

    public static <T> ResponseResult<T> empty(int code) {
        return new ResponseResult<>(code, "暂无数据", null, LoadingState.EMPTY);
    }

    public static <T> ResponseResult<T> empty() {
        return empty(LoadingState.EMPTY);
    }

}
