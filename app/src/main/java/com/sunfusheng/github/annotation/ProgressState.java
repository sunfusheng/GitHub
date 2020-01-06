package com.sunfusheng.github.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sunfusheng on 2018/4/13.
 */
@IntDef({ProgressState.START, ProgressState.PROGRESS, ProgressState.PAUSE, ProgressState.CANCEL,
        ProgressState.SUCCESS, ProgressState.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface ProgressState {
    int START = 0;
    int PROGRESS = 1;
    int PAUSE = 2;
    int CANCEL = 3;
    int SUCCESS = 4;
    int ERROR = 5;
}