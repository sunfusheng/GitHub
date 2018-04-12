package com.sunfusheng.github.net.exception;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class ServerException extends RuntimeException {

    public int code;
    public String msg;

    public ServerException(String message, Throwable cause, int code) {
        super(message, cause);
        this.msg = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return msg + "【code=" + code + "】";
    }
}
