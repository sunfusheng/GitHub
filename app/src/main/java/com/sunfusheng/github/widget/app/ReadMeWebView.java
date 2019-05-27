package com.sunfusheng.github.widget.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.sunfusheng.github.R;
import com.sunfusheng.github.util.ViewUtil;
import com.sunfusheng.github.util.readme.ReadmeHtmlUtil;

/**
 * @author by sunfusheng on 2019/1/25
 */
public class ReadMeWebView extends WebView {

    public ReadMeWebView(Context context) {
        this(context, null);
    }

    public ReadMeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadMeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        setBackgroundColor(getResources().getColor(R.color.white));

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        setWebChromeClient(new ChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWebViewClient(new WebClientLollipop());
        } else {
            setWebViewClient(new WebClient());
        }
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCachePath(getContext().getCacheDir().getPath());
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDefaultTextEncodingName("utf-8");
        boolean isLoadImageEnable = true;
        settings.setLoadsImagesAutomatically(isLoadImageEnable);
        settings.setBlockNetworkImage(!isLoadImageEnable);
        setOnLongClickListener(v -> {
//            HitTestResult result = getHitTestResult();
//            if (hitLinkResult(result) && !StringUtils.isBlank(result.getExtra())) {
//                AppUtils.copyToClipboard(getContext(), result.getExtra());
//                return true;
//            }
            return false;
        });
    }

    public void setReadme(@Nullable String repoFullName, @NonNull String readme) {
        if (TextUtils.isEmpty(repoFullName) || TextUtils.isEmpty(readme)) {
            Logger.e("ReadMeWebView TextUtils.isEmpty(repoFullName) || TextUtils.isEmpty(readme)");
            return;
        }

        String baseUrl = "https://github.com/" + repoFullName + "/blob/master/README.md";
        String page = ReadmeHtmlUtil.generateMdHtml(readme, baseUrl, false,
                getWebBackgroundColor(), getAccentColor(), false);
        loadPage(page);
    }

    private void loadPage(String page) {
        loadPageWithBaseUrl("file:///android_asset/code_prettify/", page);
    }

    private void loadPageWithBaseUrl(final String baseUrl, final String page) {
        post(() -> loadDataWithBaseURL(baseUrl, page, "text/html", "utf-8", null));
    }

    private String getWebBackgroundColor() {
        return "#" + Integer.toHexString(ViewUtil.getCommonBackground(getContext())).toUpperCase();
    }

    private String getAccentColor() {
        return "#" + Integer.toHexString(ViewUtil.getAccentColor(getContext())).substring(2).toUpperCase();
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class WebClientLollipop extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            startActivity(request.getUrl());
            return true;
        }
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            startActivity(Uri.parse(url));
            return true;
        }
    }

    private void startActivity(Uri uri) {
        if (uri == null) return;
//        AppOpener.launchUrl(getContext(), uri);
    }
}
