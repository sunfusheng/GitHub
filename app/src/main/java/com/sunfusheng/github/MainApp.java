package com.sunfusheng.github;

import android.app.Application;

import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.init(this);
    }
}
