package com.sunfusheng.github.ui.repo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.base.BaseActivity;
import com.sunfusheng.github.util.StatusBarUtil;

/**
 * @author by sunfusheng on 2018/11/14
 */
public class RepoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(this, null);
        setContentView(R.layout.layout_framelayout);
        initFragment();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, RepoDetailFragment.instance(getIntent().getStringExtra(Constants.Bundle.REPO_FULL_NAME)));
        fragmentTransaction.commitAllowingStateLoss();
    }
}
