package com.sunfusheng.github.datasource;

/**
 * @author sunfusheng on 2018/4/13.
 */
public class UserDataSource {

    private static UserDataSource instance = new UserDataSource();

    private UserDataSource() {
    }

    public static UserDataSource instance() {
        return instance;
    }

}
