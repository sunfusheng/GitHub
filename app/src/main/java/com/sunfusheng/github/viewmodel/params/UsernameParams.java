package com.sunfusheng.github.viewmodel.params;

import android.support.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.response.ResponseData;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class UsernameParams extends BaseParams {
    public String username;

    public UsernameParams(String username, @FetchMode int fetchMode) {
        super(fetchMode);
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return "UsernameParams{" +
                "username='" + username + '\'' +
                ", fetchMode=" + ResponseData.getFetchModeString(fetchMode) +
                '}';
    }
}
