package com.sunfusheng.github.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sunfusheng on 2018/4/13.
 */
@IntDef({QueryResult.NONE, QueryResult.EXIST})
@Retention(RetentionPolicy.SOURCE)
public @interface QueryResult {
    int NONE = 0;
    int EXIST = 1;
}
