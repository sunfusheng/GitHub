package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.ui.navigation.NavigationManager;
import com.sunfusheng.github.R;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.widget.bottombar.AlphaTabLayout;
import com.sunfusheng.github.widget.bottombar.AlphaTabView;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItem;
import com.sunfusheng.github.widget.bottombar.FragmentPagerItemAdapter;
import com.sunfusheng.github.widget.titlebar.CustomTitleBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

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
        initTitleBar();
        initView();
        initFragment();
    }

    private void initTitleBar() {
        CustomTitleBar titleBar = findViewById(R.id.titleBar);
        titleBar.setTitle(R.string.app_name);
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
        viewPager.addOnPageChangeListener(this);
        tabLayout.setViewPager(viewPager);
    }

    private void toLoginActivity() {
        if (!PreferenceUtil.getInstance().contains(Constants.PreferenceKey.USERNAME) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.PASSWORD) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.AUTH) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.TOKEN)) {
            NavigationManager.toLoginActivity();
            finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
