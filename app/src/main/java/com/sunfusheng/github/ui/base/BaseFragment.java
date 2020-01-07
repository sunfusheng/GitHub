package com.sunfusheng.github.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.widget.dialog.ProgressDialogHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author sunfusheng on 2018/4/11.
 */
abstract public class BaseFragment extends RxFragment {
    protected View vRootView;
    protected View statusBar;
    protected Toolbar toolbar;
    protected ProgressDialogHelper progressDialogHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(inflateLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vRootView = view;
        initToolBar();
        initView(view);
    }

    abstract public void initData(@Nullable Bundle arguments);

    @LayoutRes
    abstract public int inflateLayout();

    abstract public void initView(@NonNull View rootView);

    protected void initToolBar() {
        if (vRootView != null) {
            statusBar = vRootView.findViewById(R.id.statusBar);
            if (statusBar != null) {
                ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
                layoutParams.height = StatusBarUtil.getStatusBarHeight(AppUtil.getContext());
                statusBar.setLayoutParams(layoutParams);
            }
            toolbar = vRootView.findViewById(R.id.toolbar);
        }
    }

    protected void showProgressDialog() {
        showProgressDialog(R.string.com_waiting);
    }

    protected void showProgressDialog(int resId) {
        if (progressDialogHelper == null) {
            progressDialogHelper = new ProgressDialogHelper(getActivity());
        }
        progressDialogHelper.setMessage(resId);
        progressDialogHelper.show();
    }

    protected void dismissProgressDialog() {
        if (progressDialogHelper != null && progressDialogHelper.isShowing()) {
            progressDialogHelper.dismiss();
        }
    }
}
