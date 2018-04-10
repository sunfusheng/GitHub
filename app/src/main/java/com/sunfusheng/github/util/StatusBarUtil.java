package com.sunfusheng.github.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author by sunfusheng on 18/4/10.
 */
public class StatusBarUtil {

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAVIGATION_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private final Window window;

    public StatusBarUtil(Window window) {
        this.window = window;
    }

    public void fullScreen() {
        fullScreenOnBehindJellyBean();
        fullScreenOnOverJellyBean();
    }

    private void fullScreenOnOverJellyBean() {
        if (OsVersionUtil.hasJellyBean()) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void fullScreenOnBehindJellyBean() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setWindowImmersive(Window window) {
        if (OsVersionUtil.hasLollipop()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (OsVersionUtil.hasKitKat()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setStatusBarColor(Window window, @ColorInt int color) {
        if (OsVersionUtil.hasLollipop()) {
            window.setStatusBarColor(color);
        }
    }

    public static void fullScreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (OsVersionUtil.hasJellyBean()) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    public static void unfullScreen(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (OsVersionUtil.hasJellyBean()) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 获取状态栏高度，如果获取失败返回-1
     */
    public static int getStatusBarHeight(Context context) {
        return getInternalDimensionSize(context, STATUS_BAR_HEIGHT_RES_NAME);
    }

    public static int getNavigationBarHeight(Context context) {
        return getInternalDimensionSize(context, NAVIGATION_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Context context, String key) {
        Resources resources = context.getResources();
        int result = -1;
        int resourceId = resources.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isTranslucentStatus(Activity activity) {
        boolean translucentStatus = false;
        if (OsVersionUtil.hasKitKat()) {
            int[] attrs = {android.R.attr.windowTranslucentStatus};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                translucentStatus = a.getBoolean(0, false);
            } finally {
                a.recycle();
            }

            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (OsVersionUtil.hasLollipop()) {
                if ((winParams.flags & bits) != 0 || (winParams.flags & bits) == 0 && win.getStatusBarColor() == Color.TRANSPARENT) {
                    translucentStatus = true;
                }
            } else {
                if ((winParams.flags & bits) != 0) {
                    translucentStatus = true;
                }
            }
        }
        return translucentStatus;
    }

    // 设置状态栏透明与字体颜色
    public static void setStatusBarTranslucent(Activity activity, boolean isLightStatusBar) {
        if (activity == null) return;
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        if (isXiaomi()) {
            setXiaomiStatusBar(window, isLightStatusBar);
        } else if (isMeizu()) {
            setMeizuStatusBar(window, isLightStatusBar);
        }
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    // 设置小米状态栏
    public static void setXiaomiStatusBar(Window window, boolean isLightStatusBar) {
        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isLightStatusBar ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 是否是魅族手机
    public static boolean isMeizu() {
        try {
            Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }

    // 设置魅族状态栏
    public static void setMeizuStatusBar(Window window, boolean isLightStatusBar) {
        WindowManager.LayoutParams params = window.getAttributes();
        try {
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(params);
            if (isLightStatusBar) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(params, value);
            window.setAttributes(params);
            darkFlag.setAccessible(false);
            meizuFlags.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
