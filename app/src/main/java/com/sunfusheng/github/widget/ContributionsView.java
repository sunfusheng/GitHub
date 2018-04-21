package com.sunfusheng.github.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.DisplayUtil;
import com.sunfusheng.github.util.NetworkUtil;

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

        //设置渲染的优先级
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 开启 DOM storage API 功能
        getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getContext().getCacheDir().getAbsolutePath() + "webview_cache";
        //设置数据库缓存路径
        getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        getSettings().setAppCacheEnabled(true);

        getSettings().setLoadWithOverviewMode(true);
        //设置可以访问文件
        getSettings().setAllowFileAccess(true);
        getSettings().setDefaultTextEncodingName("UTF-8");

        setVerticalFadingEdgeEnabled(false);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(true);

        if (NetworkUtil.isConnected()) {
            //有网络连接，设置默认缓存模式
            getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //无网络连接，设置本地缓存模式
            getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        post(() -> scrollTo(DisplayUtil.getWindowWidth(getContext()), 0));
    }

    public void loadContributions(String username) {
        this.username = username;
        loadUrl("https://github.com/users/" + username + "/contributions");
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
