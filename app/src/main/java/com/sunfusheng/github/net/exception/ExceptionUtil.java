package com.sunfusheng.github.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

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

    public static String handleException(Throwable throwable) {
        return checkException(throwable).toString();
    }

    public static ResponseException checkException(Throwable throwable) {
        if (throwable == null) {
            return new ResponseException("未知错误", new RuntimeException("Unknown Error!"), UNKNOWN);
        }

        ResponseException ex;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex = new ResponseException("授权异常，未授权", throwable, UNAUTHORIZED);
                    break;
                case FORBIDDEN:
                    ex = new ResponseException("请求异常，拒绝执行", throwable, FORBIDDEN);
                    break;
                case NOT_FOUND:
                    ex = new ResponseException("请求失败，资源未找到", throwable, NOT_FOUND);
                    break;
                case REQUEST_TIMEOUT:
                    ex = new ResponseException("请求超时", throwable, REQUEST_TIMEOUT);
                    break;
                case UNPROCESSABLE_ENTITY:
                    ex = new ResponseException("请求参数错误，无法响应", throwable, REQUEST_TIMEOUT);
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex = new ResponseException("服务器异常", throwable, INTERNAL_SERVER_ERROR);
                    break;
                case BAD_GATEWAY:
                    ex = new ResponseException("网关异常", throwable, BAD_GATEWAY);
                    break;
                case SERVICE_UNAVAILABLE:
                    ex = new ResponseException("服务不可用", throwable, SERVICE_UNAVAILABLE);
                    break;
                case GATEWAY_TIMEOUT:
                    ex = new ResponseException("网关超时", throwable, GATEWAY_TIMEOUT);
                    break;
                default:
                    ex = new ResponseException("网络异常", throwable, NETWORK_ERROR);
                    break;
            }
        } else if (throwable instanceof ServerException) {
            ServerException serverException = (ServerException) throwable;
            ex = new ResponseException(serverException.msg, serverException, serverException.code);
        } else if (throwable instanceof JsonParseException || throwable instanceof JSONException || throwable instanceof ParseException) {
            ex = new ResponseException("数据解析异常", throwable, PARSE_ERROR);
        } else if (throwable instanceof ConnectException) {
            ex = new ResponseException("连接失败", throwable, CONNECT_ERROR);
        } else if (throwable instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseException("证书验证失败", throwable, SSL_ERROR);
        } else if (throwable instanceof SocketTimeoutException) {
            ex = new ResponseException("连接超时", throwable, SOCKET_TIMEOUT);
        } else if (throwable instanceof UnknownHostException) {
            ex = new ResponseException("未知主机异常", throwable, UNKNOWN_HOST);
        } else {
            ex = new ResponseException("未知错误", throwable, UNKNOWN);
        }
        return ex;
    }
}
