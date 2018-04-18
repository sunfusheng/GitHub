package com.sunfusheng.github.net;

import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.util.ExceptionUtil;

import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class ResponseResult<T> {

    public int code;
    public String msg;
    public T data;

    @LoadingState
    public int loadingState;

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(int code, String msg, T data, @LoadingState int loadingState) {
        this(code, msg, data);
        this.loadingState = loadingState;
    }

    public ResponseResult(Response<T> response, @LoadingState int loadingState) {
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
                ", loadingState=" + loadingState +
                ", data=" + data +
                '}';
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

    public static <T> ResponseResult<T> error(Throwable throwable) {
        return error(ExceptionUtil.handleException(throwable));
    }

    public static <T> ResponseResult<T> error(int errorCode) {
        return error(ExceptionUtil.getResponseExceptionByErrorCode(errorCode));
    }

    public static <T> ResponseResult<T> empty() {
        return new ResponseResult<>(LoadingState.EMPTY, "暂无数据", null, LoadingState.EMPTY);
    }

}
