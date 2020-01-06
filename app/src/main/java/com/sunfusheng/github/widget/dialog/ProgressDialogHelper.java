package com.sunfusheng.github.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.StringRes;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.function.Consumer;

public class ProgressDialogHelper {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Context context;

    private LoadingDialog pd;

    private int titleResId = R.string.com_tip;
    private int messageResId = R.string.com_waiting;
    private int positiveButtonTextId = R.string.com_ok;
    private int negativeButtonTextId = R.string.com_cancel;

    private boolean cancelable = true;
    private boolean canceledOnTouchOutside;
    private String dialogMsg;
    private Consumer<ProgressDialogHelper> confirmClick;
    private DialogInterface.OnCancelListener cancelListener;

    public ProgressDialogHelper(Context context) {
        this.context = context;
    }

    public ProgressDialogHelper setTitle(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public ProgressDialogHelper setMessage(@StringRes int messageResId) {
        this.messageResId = messageResId;
        return this;
    }

    public ProgressDialogHelper setPositiveButtonText(int positiveButtonTextId) {
        this.positiveButtonTextId = positiveButtonTextId;
        return this;
    }

    public ProgressDialogHelper setNegativeButtonText(int negativeButtonTextId) {
        this.negativeButtonTextId = negativeButtonTextId;
        return this;
    }

    public ProgressDialogHelper setCancel(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public ProgressDialogHelper setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public ProgressDialogHelper setConfirmDialog(int dialogMsgId) {
        dialogMsg = context.getString(dialogMsgId);
        return this;
    }

    public ProgressDialogHelper setConfirmDialog(String dialogMsg) {
        this.dialogMsg = dialogMsg;
        return this;
    }

    public ProgressDialogHelper setConfirmClick(Consumer<ProgressDialogHelper> consumer) {
        confirmClick = consumer;
        return this;
    }

    public ProgressDialogHelper setCancelListener(DialogInterface.OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    public ProgressDialogHelper show() {
        if (!TextUtils.isEmpty(dialogMsg)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(titleResId)
                    .setMessage(dialogMsg)
                    .setPositiveButton(positiveButtonTextId, (dialog, which) -> doing())
                    .setNegativeButton(negativeButtonTextId, null)
                    .setOnCancelListener(dialog -> {
                        if (null != cancelListener) {
                            cancelListener.onCancel(dialog);
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(cancelable);
            alertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            handler.post(alertDialog::show);
        } else {
            doing();
        }
        return this;
    }

    public boolean isShowing() {
        return pd != null && pd.isShowing();
    }

    public void dismiss() {
        if (null != pd && pd.isShowing()) {
            pd.dismiss();
        }
        pd = null;
    }

    private void doing() {
        if (null != confirmClick) {
            confirmClick.accept(this);
        } else {
            showProgress();
        }
    }

    private void showProgress() {
        if (pd == null) {
            pd = LoadingDialog.newInstance(context);
            pd.setText(context.getString(messageResId));
            pd.setCancelable(cancelable);
            pd.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }
        pd.show();
    }
}
