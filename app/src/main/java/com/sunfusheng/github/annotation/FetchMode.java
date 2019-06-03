package com.sunfusheng.github.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sunfusheng on 2018/4/13.
 */
@IntDef({FetchMode.LOCAL, FetchMode.REMOTE, FetchMode.FORCE_REMOTE})
@Retention(RetentionPolicy.SOURCE)
public @interface FetchMode {
    int LOCAL = 1;
    int REMOTE = 2;
    int FORCE_REMOTE = 3;
}
