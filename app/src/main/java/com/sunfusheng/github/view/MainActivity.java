package com.sunfusheng.github.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.NavigationManager;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.glideimageview.GlideImageView;

public class MainActivity extends BaseActivity {

    private GlideImageView vUserAvatar;
    private TextView vUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toLoginActivity();
        setContentView(R.layout.activity_main);

        vUserAvatar = findViewById(R.id.user_avatar);
        vUserName = findViewById(R.id.username);
        String username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);

        ViewModelProviders.of(this).get(UserViewModel.class)
                .getUser(username, FetchMode.REMOTE)
                .observe(this, it -> {
                    if (it == null) return;
                    switch (it.loadingState) {
                        case LoadingState.LOADING:
                            showProgressDialog();
                            break;
                        case LoadingState.SUCCESS:
                            dismissProgressDialog();
                            vUserAvatar.loadImage(it.data.getAvatar_url(), R.mipmap.ic_launcher);
                            vUserName.setText(it.data.getName());
                            break;
                        case LoadingState.ERROR:
                            dismissProgressDialog();
                            vUserName.setText(it.errorString());
                            break;
                        case LoadingState.EMPTY:
                            dismissProgressDialog();
                            vUserName.setText(it.msg);
                            break;
                    }
                });
    }

    private void toLoginActivity() {
        if (!PreferenceUtil.getInstance().contains(Constants.PreferenceKey.USERNAME) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.PASSWORD) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.AUTH) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.TOKEN)) {
            NavigationManager.toLoginActivity();
            finish();
        }
    }

}
