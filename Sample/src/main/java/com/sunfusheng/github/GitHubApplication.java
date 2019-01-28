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

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()
//                .penaltyLog()
//                .build());
//
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());

        AppUtil.init(this);
        MultiTypeRegistry.getInstance().registerDefaultBinder(new NonsupportBinder());
    }
}
