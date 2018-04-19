package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.widget.multistate.MultiStateLayout;
import com.sunfusheng.glideimageview.GlideImageView;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment {

    private MultiStateLayout multiStateLayout;
    private LinearLayout vProfile;
    private GlideImageView vAvatar;
    private TextView vName;
    private TextView vInfo;
    private LinearLayout vRepo;
    private TextView vRepoCount;
    private LinearLayout vFollowing;
    private TextView vFollowingCount;
    private LinearLayout vFollowers;
    private TextView vFollowersCount;

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
        vProfile = view.findViewById(R.id.profile);
        vAvatar = view.findViewById(R.id.avatar);
        vName = view.findViewById(R.id.name);
        vInfo = view.findViewById(R.id.info);
        vRepo = view.findViewById(R.id.repo);
        vRepoCount = view.findViewById(R.id.repo_count);
        vFollowing = view.findViewById(R.id.following);
        vFollowingCount = view.findViewById(R.id.following_count);
        vFollowers = view.findViewById(R.id.followers);
        vFollowersCount = view.findViewById(R.id.followers_count);
        multiStateLayout = view.findViewById(R.id.multiStateLayout);

        multiStateLayout.setNormalView(vProfile);
        multiStateLayout.setErrorButtonListener(v -> {
            UserViewModel.of(this).setParams(username, FetchMode.DEFAULT);
        });

        multiStateLayout.setEmptyViewListener(v -> {
            UserViewModel.of(this).setParams(username + "s", FetchMode.REMOTE);
        });

        UserViewModel.of(this).setParams(username + "s", FetchMode.LOCAL);

        UserViewModel.of(this).liveData.observe(this, it -> {
            multiStateLayout.setLoadingState(it.loadingState, () -> {
                initUserProfile(it.data);
            }, () -> {
                multiStateLayout.setErrorTip(it.errorString());
            });
        });
    }

    private void initUserProfile(User user) {
        vAvatar.loadImage(user.getAvatar_url(), R.color.white);
        vName.setText(user.getName());
        vInfo.setText(user.getBio());
        vRepoCount.setText(String.valueOf(user.getPublic_repos()));
        vFollowingCount.setText(String.valueOf(user.getFollowing()));
        vFollowersCount.setText(String.valueOf(user.getFollowers()));
    }
}
