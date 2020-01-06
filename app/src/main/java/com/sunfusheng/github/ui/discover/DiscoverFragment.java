package com.sunfusheng.github.ui.discover;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItemAdapter;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String[] TAB_NAMES = new String[]{"Daily", "Weekly", "Monthly"};

    @Override
    public void initData(@Nullable Bundle arguments) {

    }

    @Override
    public int inflateLayout() {
        return R.layout.fragment_discover;
    }

    @Override
    public void initView(@NonNull View rootView) {
        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);

        initViewPager();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        toolbar.setTitle("Tending");
    }

    private void initViewPager() {
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[0]), true);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[1]), false);
        tabLayout.addTab(tabLayout.newTab().setText(TAB_NAMES[2]), false);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter.Builder(getContext(), getChildFragmentManager())
                .add(TAB_NAMES[0], TendingRepoListFragment.newFragment("daily"))
                .add(TAB_NAMES[1], TendingRepoListFragment.newFragment("weekly"))
                .add(TAB_NAMES[2], TendingRepoListFragment.newFragment("monthly"))
                .build();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }
}
