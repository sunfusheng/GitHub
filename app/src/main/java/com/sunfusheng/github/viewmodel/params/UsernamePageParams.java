package com.sunfusheng.github.viewmodel.params;

import com.sunfusheng.github.annotation.FetchMode;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class UsernamePageParams extends PageParams {
    public String username;

    public UsernamePageParams(String username, int page, int pageCount, @FetchMode int fetchMode) {
        super(page, pageCount, fetchMode);
        this.username = username;
    }
}
