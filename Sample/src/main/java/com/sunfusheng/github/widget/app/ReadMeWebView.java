package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sunfusheng.github.R;

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

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.white));
        setOnLongClickListener(v -> true);

//        getSettings().setDefaultFontSize(12);
//        getSettings().setSupportZoom(false);
        getSettings().setDefaultTextEncodingName("UTF-8");

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
    }

    public void loadData(String data) {
        loadData(data, "text/html; charset=UTF-8", null);
    }

}
