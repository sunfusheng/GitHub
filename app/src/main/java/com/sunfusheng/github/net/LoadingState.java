package com.sunfusheng.github.net;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({LoadingState.LOADING, LoadingState.SUCCESS, LoadingState.ERROR, LoadingState.EMPTY})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadingState {
    int LOADING = 0;
    int SUCCESS = 1;
    int ERROR = 2;
    int EMPTY = 3;
}