package com.sunfusheng.github.ui;

import android.content.Context;
import android.content.Intent;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.AppUtil;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class NavigationManager {

    private static Context context = AppUtil.getContext();

    public static void toLoginActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void toMainActivity() {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void toUserActivity(User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(Constants.Bundle.USERNAME, user.login);
        context.startActivity(intent);
    }
}
