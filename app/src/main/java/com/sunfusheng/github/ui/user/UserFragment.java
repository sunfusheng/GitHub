package com.sunfusheng.github.ui.user;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.ui.base.FragmentViewPager2Adapter;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.DisplayUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.viewmodel.UserDetailViewModel;
import com.sunfusheng.github.viewmodel.vm.VMProvider;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableHelper;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableLayout;
import com.sunfusheng.github.widget.app.UserContributionsView;
import com.sunfusheng.github.widget.app.UserFollowView;
import com.sunfusheng.github.widget.app.UserProfileView;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.transformation.BlurTransformation;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment {

    private ScrollableLayout vScrollableLayout;
    private TabLayout vTabLayout;
    private ViewPager2 vViewPager2;

    private GlideImageView vToolbarBg;
    private UserFollowView vUserFollow;
    private UserProfileView vUserProfile;
    private UserContributionsView vUserContributions;

    private static final int REPOSITORIES = 0;
    private static final int EVENTS = 1;
    private static final int STARRED = 2;
    private static final int FOLLOWERS = 3;
    private static final int FOLLOWING = 4;
    private int[] TAB_NAMES = new int[]{R.string.Repositories, R.string.Events, R.string.Starred, R.string.Followers, R.string.Following};

    public static UserFragment instance(String username) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(@Nullable Bundle arguments) {

    }

    @Override
    public int inflateLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void initView(@NonNull View rootView) {
        vUserFollow = rootView.findViewById(R.id.user_follow);
        vUserProfile = rootView.findViewById(R.id.userProfile);
        vUserContributions = rootView.findViewById(R.id.userContributions);

        vScrollableLayout = rootView.findViewById(R.id.scrollableLayout);
        vTabLayout = rootView.findViewById(R.id.tabLayout);
        vViewPager2 = rootView.findViewById(R.id.viewPager2);

        initHeader();
        observeFetchUser();
        observeData();
        initViewPager();
    }

    private void initHeader() {
        statusBar.setBackgroundResource(R.color.transparent);
        toolbar.setBackgroundResource(R.color.transparent);

        vToolbarBg = vRootView.findViewById(R.id.toolbar_bg);
        vToolbarBg.setAlpha(0);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) vToolbarBg.getLayoutParams();
        int toolbarAndStatusBarHeight = toolbar.getLayoutParams().height + StatusBarUtil.getStatusBarHeight(AppUtil.getContext());
        int height = layoutParams.height - toolbarAndStatusBarHeight;
        layoutParams.setMargins(0, -height, 0, 0);
        vToolbarBg.setLayoutParams(layoutParams);
        vScrollableLayout.setMarginTop(toolbarAndStatusBarHeight);

        int distance = DisplayUtil.dp2px(getContext(), 220) - toolbarAndStatusBarHeight;
        vScrollableLayout.setOnScrollListener((scrollY, offsetY, maxY) -> {
            float alpha = offsetY * 1f / (distance);
            if (alpha > 1f) alpha = 1f;
            vToolbarBg.setAlpha((int) (alpha * 255));
        });
    }

    private void observeFetchUser() {
        UserDetailViewModel viewModel = VMProvider.of(this, UserDetailViewModel.class);
        viewModel.liveData.observe(this, it -> {
            if (it == null) return;
            vUserProfile.setLoadingState(it.loadingState);
            if (it.loadingState == LoadingState.SUCCESS) {
                User user = it.data;
                vUserProfile.setUser(user);
                toolbar.setTitle(Utils.getUsernameDesc(user));
                toolbar.setSubtitle(Utils.getCreatedTimeDesc(user.created_at));
                vToolbarBg.load(user.avatar_url, R.mipmap.ic_blur_default, new BlurTransformation(getContext(), 25, 20));
            }
        });

        viewModel.request(mUsername, FetchMode.REMOTE);
    }

    private void observeData() {
        vUserFollow.setUsername(mUsername);
        vUserContributions.setUsername(mUsername);
    }

    private void initViewPager() {
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[REPOSITORIES]), true);
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[EVENTS]), false);
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[STARRED]), false);
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[FOLLOWERS]), false);
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[FOLLOWING]), false);

        SparseArray<Fragment> fragments = new SparseArray<>();
        fragments.put(REPOSITORIES, OwnedRepoListFragment.newFragment(mUsername));
        fragments.put(EVENTS, EventsFragment.newFragment(mUsername));
        fragments.put(STARRED, StarredRepoListFragment.newFragment(mUsername));
        fragments.put(FOLLOWERS, FollowersFragment.newFragment(mUsername));
        fragments.put(FOLLOWING, FollowingFragment.newFragment(mUsername));

        FragmentViewPager2Adapter viewPager2Adapter = new FragmentViewPager2Adapter(this);
        viewPager2Adapter.setFragments(fragments);
        vViewPager2.setAdapter(viewPager2Adapter);
        new TabLayoutMediator(vTabLayout, vViewPager2, (tab, position) -> {
            tab.setText(TAB_NAMES[position]);
        }).attach();

        vViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                vScrollableLayout.getHelper().setScrollableViewContainer((ScrollableHelper.ScrollableViewContainer) viewPager2Adapter.getFragment(position));
            }
        });
    }
}
