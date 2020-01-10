package com.sunfusheng.github.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author sunfusheng
 * @since 2020-01-10
 */
public class KeyboardUtil {

    private static InputMethodManager sInputMethodManager;

    public static InputMethodManager getInputMethodManager() {
        if (sInputMethodManager == null) {
            sInputMethodManager = (InputMethodManager) AppUtil.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return sInputMethodManager;
    }


    // 显示键盘
    public static void showKeyboard(EditText editText) {
        editText.setCursorVisible(true);
        editText.requestFocus();
        getInputMethodManager().showSoftInput(editText, 0);
    }

    // 隐藏键盘
    public static void hideKeyboard(View v) {
        getInputMethodManager().hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }
}
