package com.sunfusheng.github.ui.discover;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.ui.base.FragmentViewPager2Adapter;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {
    private TabLayout vTabLayout;
    private ViewPager2 vViewPager2;

    private static final int DAILY = 0;
    private static final int WEEKLY = 1;
    private static final int MONTHLY = 2;
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
        vTabLayout = rootView.findViewById(R.id.tabLayout);
        vViewPager2 = rootView.findViewById(R.id.viewPager2);

        initViewPager();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        toolbar.setTitle(R.string.tab_discover);
    }

    private void initViewPager() {
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[DAILY]), true);
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[WEEKLY]), false);
        vTabLayout.addTab(vTabLayout.newTab().setText(TAB_NAMES[MONTHLY]), false);

        SparseArray<Fragment> fragments = new SparseArray<>();
        fragments.put(DAILY, TrendRepoListFragment.newFragment("daily"));
        fragments.put(WEEKLY, TrendRepoListFragment.newFragment("weekly"));
        fragments.put(MONTHLY, TrendRepoListFragment.newFragment("monthly"));

        FragmentViewPager2Adapter viewPager2Adapter = new FragmentViewPager2Adapter(this);
        viewPager2Adapter.setFragments(fragments);
        vViewPager2.setAdapter(viewPager2Adapter);
        new TabLayoutMediator(vTabLayout, vViewPager2, (tab, position) -> {
            tab.setText(TAB_NAMES[position]);
        }).attach();
    }
}
