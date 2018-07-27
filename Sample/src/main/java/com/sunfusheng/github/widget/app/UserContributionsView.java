package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.ProgressState;
import com.sunfusheng.github.viewmodel.ContributionsViewModel;
import com.sunfusheng.github.viewmodel.base.VM;
import com.sunfusheng.github.viewmodel.base.VmProvider;

import java.io.File;

/**
 * @author sunfusheng on 2018/7/28.
 */
public class UserContributionsView extends LinearLayout {

    private ContributionsWebView webView;
    private TextView vTip;

    public UserContributionsView(Context context) {
        this(context, null);
    }

    public UserContributionsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserContributionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_user_contributions, this);
        webView = findViewById(R.id.contributions);
        vTip = findViewById(R.id.tip);

        webView.setVisibility(GONE);
        vTip.setVisibility(VISIBLE);
        vTip.setText("Loading...");
    }

    public void setUsername(String username) {
        ContributionsViewModel vm = VmProvider.of(getContext(), ContributionsViewModel.class);
        vm.setRequestParams(username);

        vm.liveData.observe(VM.getActivity(getContext()), it -> {
            if (it == null) return;

            switch (it.progressState) {
                case ProgressState.START:
                    break;
                case ProgressState.PROGRESS:
                    break;
                case ProgressState.PAUSE:
                    vTip.setText("Paused");
                    break;
                case ProgressState.CANCEL:
                    vTip.setText("Canceled");
                    break;
                case ProgressState.SUCCESS:
                case ProgressState.ERROR:
                    if (!loadFile(ContributionsViewModel.getContributionsFilePath(username))) {
                        vTip.setVisibility(VISIBLE);
                        vTip.setText("Load Failed!");
                    }
                    break;
            }
        });
    }

    private boolean loadFile(String localPath) {
        File file = new File(localPath);
        if (file.exists()) {
            vTip.setVisibility(GONE);
            webView.setVisibility(VISIBLE);
            webView.loadUrl("file://" + localPath);
            return true;
        }
        return false;
    }
}
