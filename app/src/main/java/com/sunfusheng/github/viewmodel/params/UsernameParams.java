package com.sunfusheng.github.viewmodel.params;

import com.sunfusheng.github.annotation.FetchMode;

/**
 * @author by sunfusheng on 2019-05-28
 */
public class UsernameParams extends BaseParams {
    public String username;

    public UsernameParams(String username, @FetchMode int fetchMode) {
        super(fetchMode);
        this.username = username;
    }
}
