package com.sunfusheng.github.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.sunfusheng.github.util.DisplayUtil;

/**
 * @author sunfusheng on 2018/4/20.
 */
public class ContributionsView extends WebView {

    public ContributionsView(Context context) {
        this(context, null);
    }

    public ContributionsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContributionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getSettings().setDefaultFontSize(12);
        getSettings().setSupportZoom(false);
        setVerticalFadingEdgeEnabled(false);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(true);
        post(() -> scrollTo(DisplayUtil.getWindowWidth(getContext()), 0));
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, 0, clampedX, clampedY);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, 0);
    }
}
