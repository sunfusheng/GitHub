package com.sunfusheng.github.viewmodel.params;

import android.support.annotation.NonNull;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.net.response.ResponseData;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class UsernamePageParams extends PageParams {
    public String username;

    public UsernamePageParams(String username, int page, @FetchMode int fetchMode) {
        super(page, fetchMode);
        this.username = username;
    }

    public UsernamePageParams(String username, int page, int pageCount, @FetchMode int fetchMode) {
        super(page, pageCount, fetchMode);
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return "UsernamePageParams{" +
                "username='" + username + '\'' +
                ", page=" + page +
                ", pageCount=" + pageCount +
                ", fetchMode=" + ResponseData.getFetchModeString(fetchMode) +
                '}';
    }
}
