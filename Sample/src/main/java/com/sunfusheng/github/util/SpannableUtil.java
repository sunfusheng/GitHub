package com.sunfusheng.github.util;

import android.support.annotation.ColorRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.List;

/**
 * @author sunfusheng on 2018/6/8.
 */
public class SpannableUtil {

    public static SpannableString getSpannableString(String wholeText, String targetText, @ColorRes int targetTextColor) {
        if (TextUtils.isEmpty(wholeText)) {
            return new SpannableString("");
        }

        if (TextUtils.isEmpty(targetText)) {
            return new SpannableString(wholeText);
        }

        SpannableString spannableString = new SpannableString(wholeText);
        int startIndex = wholeText.indexOf(targetText);
        if (startIndex != -1) {
            int endIndex = startIndex + targetText.length();
            spannableString.setSpan(new ForegroundColorSpan(AppUtil.getContext().getResources().getColor(targetTextColor)),
                    startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }

    public static SpannableString getSpannableString(String wholeText, @ColorRes int targetTextColor, List<String> targetTexts) {
        if (TextUtils.isEmpty(wholeText)) {
            return new SpannableString("");
        }

        if (CollectionUtil.isEmpty(targetTexts)) {
            return new SpannableString(wholeText);
        }

        int lastIndex = -1;
        SpannableString spannableString = new SpannableString(wholeText);
        for (int i = 0; i < targetTexts.size(); i++) {
            String target = targetTexts.get(i);
            if (TextUtils.isEmpty(target)) continue;
            int index = wholeText.indexOf(target, lastIndex);
            if (index != lastIndex) {
                lastIndex = index + target.length();
                spannableString.setSpan(new ForegroundColorSpan(AppUtil.getContext().getResources().getColor(targetTextColor)),
                        index, lastIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        return spannableString;
    }

    public static SpannableString getSpannableString(String wholeText, @ColorRes int targetTextColor, String... targetTexts) {
        if (TextUtils.isEmpty(wholeText)) {
            return new SpannableString("");
        }

        if (CollectionUtil.isEmpty(targetTexts)) {
            return new SpannableString(wholeText);
        }

        int lastIndex = -1;
        SpannableString spannableString = new SpannableString(wholeText);
        for (int i = 0; i < targetTexts.length; i++) {
            String target = targetTexts[i];
            if (TextUtils.isEmpty(target)) continue;
            int index = wholeText.indexOf(target, lastIndex);
            if (index != lastIndex) {
                lastIndex = index + target.length();
                spannableString.setSpan(new ForegroundColorSpan(AppUtil.getContext().getResources().getColor(targetTextColor)),
                        index, lastIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        return spannableString;
    }

}
