package com.sunfusheng.github.widget.span;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @author sunfusheng on 2018/7/22.
 */
public abstract class TouchableSpan extends ClickableSpan implements ITouchableSpan {
    @ColorInt
    private int normalTextColor;
    @ColorInt
    private int pressedTextColor;
    @ColorInt
    private int normalBackgroundColor;
    @ColorInt
    private int pressedBackgroundColor;

    private boolean isPressed;
    private boolean needUnderline = false;

    public TouchableSpan(Context context,
                         @ColorRes int normalTextColor,
                         @ColorRes int pressedTextColor,
                         @ColorRes int normalBackgroundColor,
                         @ColorRes int pressedBackgroundColor) {
        this.normalTextColor = ContextCompat.getColor(context, normalTextColor);
        this.pressedTextColor = ContextCompat.getColor(context, pressedTextColor);
        this.normalBackgroundColor = ContextCompat.getColor(context, normalBackgroundColor);
        this.pressedBackgroundColor = ContextCompat.getColor(context, pressedBackgroundColor);
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public TouchableSpan setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        return this;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public TouchableSpan setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
        return this;
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public TouchableSpan setNormalBackgroundColor(int normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
        return this;
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public TouchableSpan setPressedBackgroundColor(int pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
        return this;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean isNeedUnderline() {
        return needUnderline;
    }

    public TouchableSpan setNeedUnderline(boolean needUnderline) {
        this.needUnderline = needUnderline;
        return this;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(isPressed ? pressedTextColor : normalTextColor);
        ds.bgColor = isPressed ? pressedBackgroundColor : normalBackgroundColor;
        ds.setUnderlineText(needUnderline);
    }

    @Override
    public final void onClick(View widget) {
        if (ViewCompat.isAttachedToWindow(widget)) {
            onSpanClick(widget);
        }
    }

    public abstract void onSpanClick(View widget);
}
