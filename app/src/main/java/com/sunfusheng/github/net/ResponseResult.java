package com.sunfusheng.github.net;

import com.sunfusheng.github.net.exception.ResponseException;

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

    public ResponseResult(int code, String msg, T data, @LoadingState int loadingState) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.loadingState = loadingState;
    }

    public String errorString() {
        return "错误码：" + code + "\n" + msg;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", loadingState=" + loadingState +
                '}';
    }

    public ResponseResult(Response<T> response, @LoadingState int loadingState) {
        this(response.code(), response.message(), response.body(), loadingState);
    }

    public static <T> ResponseResult<T> loading() {
        return new ResponseResult<>(LoadingState.LOADING, "正在加载", null, LoadingState.LOADING);
    }

    public static <T> ResponseResult<T> success(Response<T> response) {
        return new ResponseResult<>(response, LoadingState.SUCCESS);
    }

    public static <T> ResponseResult<T> error(ResponseException e) {
        return new ResponseResult<>(e.code, e.msg, null, LoadingState.ERROR);
    }

    public static <T> ResponseResult<T> empty() {
        return new ResponseResult<>(LoadingState.EMPTY, "暂无数据", null, LoadingState.EMPTY);
    }
}
