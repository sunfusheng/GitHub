package com.sunfusheng.github.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.util.DisplayUtil;

/**
 * @author sunfusheng on 2018/4/20.
 */
public class ContributionsView extends WebView {

    private String username;

    public ContributionsView(Context context) {
        this(context, null);
    }

    public ContributionsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContributionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.white));

        getSettings().setDefaultFontSize(12);
        getSettings().setSupportZoom(false);
        post(() -> scrollTo(DisplayUtil.getWindowWidth(getContext()), 0));
    }

    public void loadContributions(String username) {
        this.username = username;
        String localPath = "file://" + Constants.FileDir.CONTRIBUTIONS + username + "_contributions.html";
        Log.d("--->", "localPath: " + localPath);
        loadUrl(localPath);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, 0, clampedX, clampedY);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, 0);
    }

    public void onDestroy() {
        destroy();
    }
}
