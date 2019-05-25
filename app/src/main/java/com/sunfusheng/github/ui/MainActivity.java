package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sunfusheng.FirUpdater;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.cache.disklrucache.DiskLruCache;
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

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        new FirUpdater(AppUtil.getContext())
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
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.PASSWORD) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.AUTH) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.TOKEN)) {
            NavigationManager.toLoginActivity(this);
            finish();
        }
    }

    private DiskLruCache mDiskLruCache;

    private void initDiskLruCache() {
        if (mDiskLruCache == null) {
            try {
                mDiskLruCache = DiskLruCache.open(Constants.CacheDir.README, 1, 1, 1024 * 1024 * 20);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cacheReadme(String repoFullName, String data) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            return;
        }

        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(hashKeyForDisk(repoFullName));
            OutputStream outputStream = editor.newOutputStream(0);
            outputStream.write(data.getBytes());
            outputStream.close();
            editor.commit();
            mDiskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getReadme(String repoFullName) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            return null;
        }

        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyForDisk(repoFullName));
            if (snapshot != null) {
                return snapshot.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean deleteReadme(String repoFullName) {
        initDiskLruCache();
        if (mDiskLruCache == null) {
            return false;
        }

        try {
            return mDiskLruCache.remove(hashKeyForDisk(repoFullName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将key进行加密
     *
     * @param key
     * @return
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
