package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.glideimageview.GlideImageView;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment {

    private GlideImageView vUserAvatar;
    private TextView vUserName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        String username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);

        UserViewModel.of(this).getUser(username, FetchMode.DEFAULT)
                .observe(this, it -> {
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
}
