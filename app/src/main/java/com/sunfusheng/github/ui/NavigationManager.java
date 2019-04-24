package com.sunfusheng.github.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.User;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class NavigationManager {

    public static void toLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void toMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void toUserActivity(Context context, User user) {
        toUserActivity(context, user.login);
    }

    public static void toUserActivity(Context context, String username) {
        if (TextUtils.isEmpty(username)) return;
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(Constants.Bundle.USERNAME, username);
        context.startActivity(intent);
    }

    public static void toRepoDetailActivity(Context context, String repoFullName) {
        if (TextUtils.isEmpty(repoFullName)) return;
        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtra(Constants.Bundle.REPO_FULL_NAME, repoFullName);
        context.startActivity(intent);
    }

    public static void toRepoDetailActivity(Context context, String username, String repoName) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(repoName)) return;
        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtra(Constants.Bundle.USERNAME, username);
        intent.putExtra(Constants.Bundle.REPO_NAME, repoName);
        context.startActivity(intent);
    }
}
