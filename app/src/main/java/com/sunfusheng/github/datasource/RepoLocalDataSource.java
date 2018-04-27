package com.sunfusheng.github.datasource;

/**
 * @author sunfusheng on 2018/4/27.
 */
public class RepoLocalDataSource extends LocalDataSource {

    private static RepoLocalDataSource instance = new RepoLocalDataSource();

    private RepoLocalDataSource() {
    }

    public static RepoLocalDataSource instance() {
        return instance;
    }

}
