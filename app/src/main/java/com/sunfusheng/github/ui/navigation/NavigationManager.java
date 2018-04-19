package com.sunfusheng.github.ui.navigation;

import android.content.Context;
import android.content.Intent;

import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.ui.LoginActivity;
import com.sunfusheng.github.ui.MainActivity;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class NavigationManager {

    private static Context context = AppUtil.getContext();

    public static void toLoginActivity() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void toMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
