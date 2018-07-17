package com.sunfusheng.github.util;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class ExceptionUtil {

    // HTTP异常
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    // 约定错误
    public static final int UNKNOWN = 1000; // 未知错误
    public static final int PARSE_ERROR = 1001; // 数据解析异常
    public static final int CONNECT_ERROR = 1002; // 连接失败
    public static final int NETWORK_ERROR = 1003; // 网络错误
    public static final int SSL_ERROR = 1005; // 证书验证失败
    public static final int SOCKET_TIMEOUT = 1006; // 连接超时
    public static final int UNKNOWN_HOST = 1007; // 未知主机

    public static ResponseException handleException(Response errorResponse) {
        if (errorResponse == null) {
            return unknownException();
        }
        return getResponseExceptionByErrorCode(errorResponse.code());
    }

    public static ResponseException handleException(Throwable throwable) {
        if (throwable == null) {
            return unknownException();
        }

        throwable.printStackTrace();

        ResponseException ex;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            ex = getResponseExceptionByErrorCode(httpException.code());
        } else if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof ParseException) {
            ex = new ResponseException(PARSE_ERROR, "数据解析异常", throwable);
        } else if (throwable instanceof ConnectException) {
            ex = new ResponseException(CONNECT_ERROR, "连接失败", throwable);
        } else if (throwable instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseException(SSL_ERROR, "证书验证失败", throwable);
        } else if (throwable instanceof SocketTimeoutException) {
            ex = new ResponseException(SOCKET_TIMEOUT, "连接超时", throwable);
        } else if (throwable instanceof UnknownHostException) {
            ex = new ResponseException(UNKNOWN_HOST, "未知主机异常", throwable);
        } else {
            ex = new ResponseException(throwable.hashCode(), throwable.getMessage(), throwable);
        }
        return ex;
    }

    public static ResponseException getResponseExceptionByErrorCode(int errorCode) {
        switch (errorCode) {
            case UNAUTHORIZED:
                return new ResponseException(UNAUTHORIZED, "授权异常，未授权");
            case FORBIDDEN:
                return new ResponseException(FORBIDDEN, "请求异常，拒绝执行");
            case NOT_FOUND:
                return new ResponseException(NOT_FOUND, "请求失败，资源未找到");
            case REQUEST_TIMEOUT:
                return new ResponseException(REQUEST_TIMEOUT, "请求超时");
            case UNPROCESSABLE_ENTITY:
                return new ResponseException(REQUEST_TIMEOUT, "请求参数错误，无法响应");
            case INTERNAL_SERVER_ERROR:
                return new ResponseException(INTERNAL_SERVER_ERROR, "服务器异常");
            case BAD_GATEWAY:
                return new ResponseException(BAD_GATEWAY, "网关异常");
            case SERVICE_UNAVAILABLE:
                return new ResponseException(SERVICE_UNAVAILABLE, "服务不可用");
            case GATEWAY_TIMEOUT:
                return new ResponseException(GATEWAY_TIMEOUT, "网关超时");
            default:
                return new ResponseException(NETWORK_ERROR, "网络异常");
        }
    }

    private static ResponseException unknownException() {
        return new ResponseException(UNKNOWN, "未知错误");
    }

    public static class ResponseException extends Exception {

        public int code;
        public String msg;

        public ResponseException(int code, String msg) {
            super(msg);
            this.code = code;
            this.msg = msg;
        }

        public ResponseException(int code, String msg, Throwable throwable) {
            super(msg, throwable);
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "【code=" + code + "】" + msg;
        }
    }
}
