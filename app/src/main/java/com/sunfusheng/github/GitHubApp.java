package com.sunfusheng.github;

import android.app.Application;

import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.viewbinder.NonsupportBinder;
import com.sunfusheng.multitype.MultiTypeRegistry;

/**
 * @author sunfusheng on 2018/4/8.
 */
public class GitHubApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtil.init(this);

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("GitHub")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.debugMode;
            }
        });

        MultiTypeRegistry.getInstance().registerDefaultBinder(new NonsupportBinder());
    }
}
