package com.sunfusheng.github;

import android.app.Application;

import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.viewbinder.NonsupportBinder;
import com.sunfusheng.multitype.MultiTypeRegistry;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class GitHubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtil.init(this);
        MultiTypeRegistry.getInstance().registerDefaultBinder(new NonsupportBinder());
    }
}
