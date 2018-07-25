package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.ProgressState;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.DateUtil;
import com.sunfusheng.github.util.DisplayUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.viewmodel.ContributionsViewModel;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.sunfusheng.github.widget.ListenerNestedScrollView;
import com.sunfusheng.github.widget.app.ContributionsView;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.transformation.BlurTransformation;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {

    private ListenerNestedScrollView nestedScrollView;

    private GlideImageView toolbarBackground;
    private GlideImageView profileAvatarBg;
    private GlideImageView vAvatar;
    private LinearLayout vProfile;
    private TextView vInfo;

    private LinearLayout vRepo;
    private TextView vRepoCount;
    private LinearLayout vFollowing;
    private TextView vFollowingCount;
    private LinearLayout vFollowers;
    private TextView vFollowersCount;

    private ContributionsView vContributions;
    private FrameLayout vRepoContainer;

    private String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        super.onViewCreated(view, savedInstanceState);
        initView();
        initHeader();
        observeUser();
        observeContributions();
        addRepoFragment();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.getString(Constants.Bundle.USERNAME);
        }

        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }
    }

    private void initView() {
        View view = getView();
        if (view == null) return;
        vProfile = view.findViewById(R.id.profile);
        profileAvatarBg = view.findViewById(R.id.profile_avatar_bg);
        vAvatar = view.findViewById(R.id.avatar);
        vInfo = view.findViewById(R.id.info);
        vRepo = view.findViewById(R.id.repo);
        vRepoCount = view.findViewById(R.id.repo_count);
        vFollowing = view.findViewById(R.id.following);
        vFollowingCount = view.findViewById(R.id.following_count);
        vFollowers = view.findViewById(R.id.followers);
        vFollowersCount = view.findViewById(R.id.followers_count);


        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        vContributions = view.findViewById(R.id.contributions);
        vRepoContainer = view.findViewById(R.id.repo_container);

        vRepo.setOnClickListener(this);
        vFollowing.setOnClickListener(this);
        vFollowers.setOnClickListener(this);
    }

    private void initHeader() {
        if (getView() == null) return;
        statusBar.setBackgroundResource(R.color.transparent);
        toolbar.setBackgroundResource(R.color.transparent);

        toolbarBackground = getView().findViewById(R.id.toolbarBackground);
        toolbarBackground.setAlpha(0);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbarBackground.getLayoutParams();
        int toolbarAndStatusBarHeight = toolbar.getLayoutParams().height + StatusBarUtil.getStatusBarHeight(AppUtil.getContext());
        int height = layoutParams.height - toolbarAndStatusBarHeight;
        layoutParams.setMargins(0, -height, 0, 0);
        toolbarBackground.setLayoutParams(layoutParams);

        int distance = DisplayUtil.dp2px(getContext(), 200) - toolbarAndStatusBarHeight;

        nestedScrollView.setOnScrollChangedInterface((scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < 0) scrollY = 0;
            float alpha = Math.abs(scrollY) * 1.0f / (distance);
            if (scrollY <= distance) {
                toolbarBackground.setAlpha((int) (alpha * 255));
            } else {
                toolbarBackground.setAlpha(255);
            }
        });
    }

    private void observeUser() {
        UserViewModel viewModel = VmProvider.of(this, UserViewModel.class);
        viewModel.setRequestParams(username, FetchMode.DEFAULT);

        viewModel.liveData.observe(this, it -> {
            if (it.loadingState == LoadingState.SUCCESS) {
                User user = it.data;

                toolbar.setTitle(user.name + "（" + user.login + "）");
                toolbar.setSubtitle("创建于" + DateUtil.formatDate2String(user.created_at, DateUtil.FORMAT.format(DateUtil.FORMAT.yyyyMMdd)));

                toolbarBackground.load(user.avatar_url, R.mipmap.ic_blur_default, new BlurTransformation(getContext(), 25, 20));
                profileAvatarBg.load(user.avatar_url, R.mipmap.ic_blur_default, new BlurTransformation(getContext(), 25, 20));
                vAvatar.load(user.avatar_url, R.color.background_common, 5);
                vInfo.setText("签名: " + user.bio + "\n" +
                        "公司: " + user.company + "\n" +
                        "位置: " + user.location + "\n" +
                        "博客: " + user.blog + "\n" +
                        "地址: " + user.html_url);

                vRepoCount.setText(String.valueOf(user.public_repos));
                vFollowingCount.setText(String.valueOf(user.following));
                vFollowersCount.setText(String.valueOf(user.followers));
            }
        });
    }

    private void observeContributions() {
        ContributionsViewModel viewModel = VmProvider.of(this, ContributionsViewModel.class);
        viewModel.setRequestParams(username);

        viewModel.liveData.observe(this, it -> {
            if (it.progressState == ProgressState.SUCCESS) {
                vContributions.loadContributions(it.data);
            }
        });
    }

    private void addRepoFragment() {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.repo_container, RepoFragment.newFragment(username));
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

    }
}
