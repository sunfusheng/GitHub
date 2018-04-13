package com.sunfusheng.github.net.exception;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class ResponseException extends Exception {

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
