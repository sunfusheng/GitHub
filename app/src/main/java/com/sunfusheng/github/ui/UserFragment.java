package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.sunfusheng.github.widget.multistate.MultiStateLayout;
import com.sunfusheng.glideimageview.GlideImageView;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment {

    private MultiStateLayout multiStateLayout;
    private GlideImageView vUserAvatar;
    private TextView vUserName;

    private String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.getString(Constants.Bundle.USERNAME);
        }
        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        multiStateLayout = view.findViewById(R.id.multiStateLayout);
        multiStateLayout.setErrorButtonListener(v -> {
            UserViewModel.of(this).setParams(username, FetchMode.DEFAULT);
        });

        UserViewModel.of(this).setParams(username+"s", FetchMode.DEFAULT);

        UserViewModel.of(this).liveData.observe(this, it -> {
            multiStateLayout.setLoadingState(it.loadingState);

            switch (it.loadingState) {
                case LoadingState.SUCCESS:

                    break;
                case LoadingState.ERROR:
                    multiStateLayout.setErrorTip(it.errorString());
                    break;
            }
        });
    }
}
