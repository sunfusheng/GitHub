package com.sunfusheng.github.net.download;

import com.sunfusheng.github.annotation.ProgressState;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class ProgressResult<T> {

    public int progress;
    public String errorMsg;
    public T data;
    @ProgressState
    public int progressState;

    public ProgressResult(int progressState) {
        this(0, null, progressState);
    }

    public ProgressResult(T data, int progressState) {
        this(0, data, progressState);
    }

    public ProgressResult(int progress, T data, int progressState) {
        this.progress = progress;
        this.data = data;
        this.progressState = progressState;
    }

    @Override
    public String toString() {
        return "ProgressResult{" +
                "progress=" + progress +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                ", progressState=" + progressState +
                '}';
    }

    public static <T> ProgressResult<T> start() {
        return new ProgressResult(0, null, ProgressState.START);
    }

    public static <T> ProgressResult<T> progress(int progress) {
        return new ProgressResult(progress, null, ProgressState.PROGRESS);
    }

    public static <T> ProgressResult<T> success(T data) {
        return new ProgressResult(data, ProgressState.SUCCESS);
    }

    public static <T> ProgressResult<T> error(Throwable e) {
        return error(e.getMessage());
    }

    public static <T> ProgressResult<T> error(String errorMsg) {
        ProgressResult progressResult = new ProgressResult(ProgressState.ERROR);
        progressResult.errorMsg = errorMsg;
        return progressResult;
    }
}
