package com.sunfusheng.github.ui.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.ui.repo.RepoListFragment;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.DisplayUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.viewmodel.UserDetailViewModel;
import com.sunfusheng.github.viewmodel.base.VMProviders;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableHelper;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableLayout;
import com.sunfusheng.github.widget.app.UserContributionsView;
import com.sunfusheng.github.widget.app.UserFollowView;
import com.sunfusheng.github.widget.app.UserProfileView;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItemAdapter;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.transformation.BlurTransformation;

/**
 * @author sunfusheng on 2018/4/12.
 */
public class UserFragment extends BaseFragment {

    private ScrollableLayout scrollableLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private GlideImageView vToolbarBg;
    private UserFollowView vUserFollow;
    private UserProfileView vUserProfile;
    private UserContributionsView vUserContributions;
    private ImageView vSort;

    private boolean hasInstantiate = false;
    private int[] TAB_NAMES = new int[]{R.string.Repositories, R.string.Followers, R.string.Following, R.string.Stars, R.string.Activities};
    private int currTabIndex = 0;
    private int lastTabIndex = -1;
    private String mUsername;

    public static UserFragment instance(String username) {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle arguments) {
        if (arguments != null) {
            mUsername = arguments.getString(Constants.Bundle.USERNAME);
        }

        if (TextUtils.isEmpty(mUsername)) {
            mUsername = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }

        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
    }

    @Override
    public int inflateLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void initView(@NonNull View rootView) {
        vUserFollow = rootView.findViewById(R.id.user_follow);
        vUserProfile = rootView.findViewById(R.id.user_profile);
        vUserContributions = rootView.findViewById(R.id.user_contributions);

        scrollableLayout = rootView.findViewById(R.id.scrollableLayout);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);
        vSort = rootView.findViewById(R.id.vSort);

        vUserProfile.setRepoClickListener(v -> tabLayout.getTabAt(0).select());
        vUserProfile.setFollowersClickListener(v -> tabLayout.getTabAt(1).select());
        vUserProfile.setFollowingClickListener(v -> tabLayout.getTabAt(2).select());
        vSort.setOnClickListener(v -> {
        });

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
        scrollableLayout.setMarginTop(toolbarAndStatusBarHeight);

        int distance = DisplayUtil.dp2px(getContext(), 220) - toolbarAndStatusBarHeight;
        scrollableLayout.setOnScrollListener((scrollY, offsetY, maxY) -> {
            float alpha = offsetY * 1f / (distance);
            if (alpha > 1f) alpha = 1f;
            vToolbarBg.setAlpha((int) (alpha * 255));
        });
    }

    private void observeFetchUser() {
        UserDetailViewModel viewModel = VMProviders.of(this, UserDetailViewModel.class);
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

        viewModel.request(mUsername, FetchMode.DEFAULT);
    }

    private void observeData() {
        vUserFollow.setUsername(mUsername);
        vUserContributions.setUsername(mUsername);
    }

    private void initViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[0]), true);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[1]), false);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[2]), false);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[3]), false);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[4]), false);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter.Builder(getContext(), getChildFragmentManager())
                .add(TAB_NAMES[0], RepoListFragment.newFragment(mUsername))
                .add(TAB_NAMES[1], RepoListFragment.newFragment(mUsername))
                .add(TAB_NAMES[2], RepoListFragment.newFragment(mUsername))
                .add(TAB_NAMES[3], RepoListFragment.newFragment(mUsername))
                .add(TAB_NAMES[4], RepoListFragment.newFragment(mUsername))
                .build();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(currTabIndex);
        tabLayout.setupWithViewPager(viewPager);

        adapter.setOnInstantiateFragmentListener((position, fragment, args) -> {
            if (!hasInstantiate) {
                hasInstantiate = true;
                scrollableLayout.getHelper().setScrollableViewContainer((ScrollableHelper.ScrollableViewContainer) adapter.getFragment(0));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                scrollableLayout.getHelper().setScrollableViewContainer((ScrollableHelper.ScrollableViewContainer) adapter.getFragment(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
