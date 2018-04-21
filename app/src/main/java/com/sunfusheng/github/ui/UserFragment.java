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
import com.sunfusheng.github.viewmodel.VM;
import com.sunfusheng.github.widget.ContributionsView;
import com.sunfusheng.github.widget.multistate.MultiStateView;
import com.sunfusheng.glideimageview.GlideImageView;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment {

    private MultiStateView multiStateView;
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
    private ContributionsView vContributions;

    private String username;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_multi_state, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeDataSource();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.getString(Constants.Bundle.USERNAME);
        }

        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }

        userViewModel = VM.of(this, UserViewModel.class);
    }

    private void initView() {
        multiStateView = (MultiStateView) getView();
        View view = multiStateView.setContentView(R.layout.fragment_user);
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

        userViewModel.setRequestParams(username, FetchMode.DEFAULT);
        multiStateView.setErrorButtonListener(v -> {
            userViewModel.setRequestParams(username, FetchMode.DEFAULT);
        });

        vContributions = view.findViewById(R.id.contributions);
        vContributions.loadContributions(username);
    }

    private void observeDataSource() {
        userViewModel.liveData.observe(this, it -> {
            multiStateView.setLoadingState(it.loadingState, () -> {
                initUserProfile(it.data);
            }, () -> {
                multiStateView.setErrorTip(it.errorString());
            });
        });
    }

    private void initUserProfile(User user) {
        vAvatar.loadImage(user.getAvatar_url(), R.color.white);
        vName.setText(user.getName() + "（" + user.getLogin() + "）");
        vInfo.setText(user.getBio());
        vRepoCount.setText(String.valueOf(user.getPublic_repos()));
        vFollowingCount.setText(String.valueOf(user.getFollowing()));
        vFollowersCount.setText(String.valueOf(user.getFollowers()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vContributions.destroy();
    }
}
