package com.sunfusheng.github.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.github.R;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class LoadingDialog extends Dialog {

    private LinearLayout vRoot;
    private TextView vText;

    private LoadingDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static LoadingDialog newInstance(Context context) {
        return new LoadingDialog(context, R.style.CommonDialog);
    }

    private void init(Context context) {
        View mDialogView = View.inflate(context, R.layout.common_dialog_loading, null);

        vRoot = mDialogView.findViewById(R.id.common_dialog_loading_root);
        vText = mDialogView.findViewById(R.id.common_dialog_loading_text);

        setContentView(mDialogView);

        setOnShowListener(dialog -> vRoot.setVisibility(View.VISIBLE));
    }

    public LoadingDialog setText(CharSequence text) {
        vText.setText(text);
        return this;
    }
}
