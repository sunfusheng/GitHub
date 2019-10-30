package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sunfusheng.FirUpdater;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.base.BaseActivity;
import com.sunfusheng.github.ui.discover.DiscoverFragment;
import com.sunfusheng.github.ui.home.HomeFragment;
import com.sunfusheng.github.ui.user.UserFragment;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.widget.bottombar.AlphaTabLayout;
import com.sunfusheng.github.widget.bottombar.AlphaTabView;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItem;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;
    private AlphaTabLayout tabLayout;
    private AlphaTabView homeTab;
    private AlphaTabView discoverTab;
    private AlphaTabView mineTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toLoginActivity();
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initFragment();
    }

    private void initData() {
        FirUpdater.getInstance(AppUtil.getContext())
                .apiToken("3c57fb226edf7facf821501e4eba08d2")
                .appId("5b977079959d695362ada470")
                .checkVersion();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        homeTab = tabLayout.getTabView(0);
        discoverTab = tabLayout.getTabView(1);
        mineTab = tabLayout.getTabView(2);
    }

    private void initFragment() {
        List<FragmentPagerItem> items = new ArrayList<>();
        items.add(FragmentPagerItem.create(homeTab.getText(), new HomeFragment()));
        items.add(FragmentPagerItem.create(discoverTab.getText(), new DiscoverFragment()));
        items.add(FragmentPagerItem.create(mineTab.getText(), new UserFragment()));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(this, getSupportFragmentManager(), items);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setViewPager(viewPager);
    }

    private void toLoginActivity() {
        if (!PreferenceUtil.getInstance().contains(Constants.PreferenceKey.USERNAME) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.AUTH) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.TOKEN)) {
            NavigationManager.toLoginActivity(this);
            finish();
        }
    }

}
