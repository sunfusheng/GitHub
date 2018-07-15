package com.sunfusheng.github.util;

import android.graphics.Color;
import android.text.TextUtils;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class Utils {

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
        }
        return Color.parseColor("#666666");
    }

    public static String getDesc(String type, String ref, String repoName) {
        StringBuilder sb = new StringBuilder();

        switch (type) {
            case "PushEvent":
                sb.append("Pushed ");
                break;
            default:
                sb.append(type).append(" ");
                break;
        }

        if (!TextUtils.isEmpty(ref)) {
            String[] split = ref.split("/");
            sb.append("to ");
            sb.append(split[split.length - 1]);
            sb.append(" ");
        }

        if (!TextUtils.isEmpty(repoName)) {
            sb.append("in ").append(repoName);
        }

        return sb.toString();
    }

}
