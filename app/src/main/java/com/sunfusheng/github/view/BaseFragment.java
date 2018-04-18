package com.sunfusheng.github.view;

import com.sunfusheng.github.R;
import com.sunfusheng.github.widget.dialog.ProgressDialogHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author sunfusheng on 2018/4/11.
 */
public class BaseFragment extends RxFragment {


    protected ProgressDialogHelper progressDialogHelper;

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
