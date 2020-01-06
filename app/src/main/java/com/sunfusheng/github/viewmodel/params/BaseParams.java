package com.sunfusheng.github.viewmodel.params;

import androidx.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.http.response.ResponseData;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class BaseParams {
    public int fetchMode;

    public BaseParams(@FetchMode int fetchMode) {
        this.fetchMode = fetchMode;
    }

    @NonNull
    @Override
    public String toString() {
        return "BaseParams{" +
                "fetchMode=" + ResponseData.getFetchModeString(fetchMode) +
                '}';
    }
}
