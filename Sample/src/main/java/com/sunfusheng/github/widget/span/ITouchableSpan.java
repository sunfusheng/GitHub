package com.sunfusheng.github.widget.span;

import android.view.View;

/**
 * @author sunfusheng on 2018/7/22.
 */
public interface ITouchableSpan {
    void setPressed(boolean pressed);
    void onClick(View widget);
}
