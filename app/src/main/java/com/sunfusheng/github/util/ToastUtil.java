package com.sunfusheng.github.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by sunfusheng on 17/2/22.
 */
public class ToastUtil {
    private static Toast mToast;

    public static void toast(@StringRes int resId) {
        toast(AppUtil.getApp().getString(resId), true);
    }

    public static void toast(String msg) {
        toast(msg, true);
    }

    public static void toastLong(@StringRes int resId) {
        toast(AppUtil.getApp().getString(resId), false);
    }

    public static void toastLong(String msg) {
        toast(msg, false);
    }

    @SuppressLint("ShowToast")
    public static void toast(String msg, boolean isShort) {
        if (AppUtil.isAppForeground() && !TextUtils.isEmpty(msg)) {
            int duration = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            if (msg.length() > 20) {
                duration = Toast.LENGTH_LONG;
            }

            if (mToast == null) {
                mToast = Toast.makeText(AppUtil.getApp(), msg, duration);
            }

            mToast.setText(msg);
            mToast.setDuration(duration);
            mToast.show();
        }
    }
}