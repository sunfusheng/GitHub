package com.sunfusheng.github.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * @author sunfusheng on 2018/4/21.
 */
public class ListenerNestedScrollView extends NestedScrollView {

    private OnScrollChangedListener onScrollChangedListener;

    public ListenerNestedScrollView(Context context) {
        super(context);
    }

    public ListenerNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenerNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.onScrollChangedListener = listener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
