package com.sunfusheng.github.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng on 2018/1/20.
 */
public class ViewUtil {

    public static void setBackgroundKeepingPadding(View view, Drawable drawable) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackground(drawable);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public static void setBackgroundKeepingPadding(View view, int backgroundResId) {
        setBackgroundKeepingPadding(view, view.getResources().getDrawable(backgroundResId));
    }

    public static void setBackgroundColorKeepPadding(View view, @ColorInt int color) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackgroundColor(color);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getAccentColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getPrimaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorPrimary);
    }

    @ColorInt
    public static int getSecondaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorSecondary);
    }

    @ColorInt
    public static int getWindowBackground(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.windowBackground);
    }

    @ColorInt
    public static int getCommonBackground(@NonNull Context context) {
        return context.getResources().getColor(R.color.background_common);
    }

    @ColorInt
    private static int getColorAttr(@NonNull Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }


    private static Bitmap getBitmapFromResource(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static String getRGBColor(int colorValue, boolean withAlpha) {
        int r = ((colorValue >> 16) & 0xff);
        int g = ((colorValue >> 8) & 0xff);
        int b = ((colorValue) & 0xff);
        int a = ((colorValue >> 24) & 0xff);
        String red = Integer.toHexString(r);
        String green = Integer.toHexString(g);
        String blue = Integer.toHexString(b);
        String alpha = Integer.toHexString(a);
        red = fixColor(red);
        green = fixColor(green);
        blue = fixColor(blue);
        alpha = fixColor(alpha);
        return withAlpha ? alpha + red + green + blue : red + green + blue;
    }

    private static String fixColor(@NonNull String colorStr) {
        return colorStr.length() == 1 ? "0" + colorStr : colorStr;
    }

    public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return true; // It's a light color
        } else {
            return false; // It's a dark color
        }
    }

}
