package com.sunfusheng.github.util;

import android.graphics.Color;

import java.util.Date;
import java.util.Locale;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class Utils {
    public static final String GITHUB_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static int getColorByLanguage(String language) {
        switch (language) {
            case "Java":
                return Color.parseColor("#af7126");
            case "Objective-C":
                return Color.parseColor("#4791fc");
            case "JavaScript":
                return Color.parseColor("#efde6d");
            case "C++":
                return Color.parseColor("#ef517f");
            case "Swift":
                return Color.parseColor("#fbaa59");
            case "HTML":
                return Color.parseColor("#df4e37");
            case "Python":
                return Color.parseColor("#3873a3");
            case "Go":
                return Color.parseColor("#3960a9");
        }
        return Color.parseColor("#666666");
    }

    public static String getStarCount(int starCount) {
        if (starCount > 1000) {
            float result = (starCount / 100) * 1f / 10 + (starCount % 100 >= 50 ? 0.1f : 0f);
            return String.format(Locale.getDefault(), "%.1f", result) + "k";
        }
        return String.valueOf(starCount);
    }

    public static long getMilliSeconds(String dateStr) {
        return DateUtil.getMilliSeconds(dateStr, GITHUB_PATTERN) + DateUtil.BEIJING_SECOND;
    }

    public static String getDate(String dateStr) {
        return DateUtil.formatDate(getMilliSeconds(dateStr));
    }

    public static String getDateAgo(String dateStr) {
        return DateUtil.formatTimeAgo(new Date().getTime() - getMilliSeconds(dateStr), true);
    }

}
