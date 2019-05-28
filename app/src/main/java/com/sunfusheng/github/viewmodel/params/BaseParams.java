package com.sunfusheng.github.viewmodel.params;

import com.sunfusheng.github.annotation.FetchMode;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class BaseParams {
    public int fetchMode;

    public BaseParams(@FetchMode int fetchMode) {
        this.fetchMode = fetchMode;
    }
}
