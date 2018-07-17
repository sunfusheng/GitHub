package com.sunfusheng.github.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sunfusheng on 2018/4/13.
 */
@IntDef({FetchMode.DEFAULT, FetchMode.LOCAL, FetchMode.REMOTE})
@Retention(RetentionPolicy.SOURCE)
public @interface FetchMode {
    int DEFAULT = 0;
    int LOCAL = 1;
    int REMOTE = 2;
}
