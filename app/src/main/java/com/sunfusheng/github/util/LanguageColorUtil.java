package com.sunfusheng.github.util;

import android.graphics.Color;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class LanguageColorUtil {

    public static int getColor(String language) {
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
        return Color.parseColor("#d6d6d6");
    }

}
