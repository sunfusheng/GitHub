package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.R;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItemAdapter;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String[] TAB_NAMES = new String[]{"Daily", "Weekly", "Monthly"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewPager();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        toolbar.setTitle("Tending");
    }

    private void initView() {
        View view = getView();
        if (view == null) return;
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
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
