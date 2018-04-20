package com.sunfusheng.github.widget.multistate;

import android.view.View;

import com.sunfusheng.github.annotation.LoadingState;

/**
 * @author by sunfusheng on 2018/4/19.
 */
public class LoadingStateDelegate {

    private View views[] = new View[4];

    public LoadingStateDelegate(View loadingView, View errorView, View emptyView) {
        this(loadingView, null, errorView, emptyView);
    }

    public LoadingStateDelegate(View loadingView, View successView, View errorView, View emptyView) {
        views[0] = loadingView;
        views[1] = successView;
        views[2] = errorView;
        views[3] = emptyView;
    }

    public void setLoadingState(@LoadingState int state) {
        if (state < 0 || state >= views.length) {
            return;
        }

        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }

        if (views[state] != null) {
            views[state].setVisibility(View.VISIBLE);
        }
    }

    public void setLoadingView(View loadingView) {
        views[0] = loadingView;
    }

    public void setSuccessView(View successView) {
        views[1] = successView;
    }

    public void setErrorView(View errorView) {
        views[2] = errorView;
    }

    public void setEmptyView(View emptyView) {
        views[3] = emptyView;
    }
}
