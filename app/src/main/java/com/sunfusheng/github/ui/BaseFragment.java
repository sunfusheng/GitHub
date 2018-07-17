package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.widget.dialog.ProgressDialogHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author sunfusheng on 2018/4/11.
 */
public class BaseFragment extends RxFragment {

    protected View statusBar;
    protected Toolbar toolbar;
    protected ProgressDialogHelper progressDialogHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar();
    }

    protected void initToolBar() {
        if (getView() != null) {
            statusBar = getView().findViewById(R.id.statusBar);
            if (statusBar != null) {
                ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
                layoutParams.height = StatusBarUtil.getStatusBarHeight(AppUtil.getContext());
                statusBar.setLayoutParams(layoutParams);
            }
            toolbar = getView().findViewById(R.id.toolbar);
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
