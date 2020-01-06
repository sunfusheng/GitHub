package com.sunfusheng.github.ui.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.base.BaseActivity;
import com.sunfusheng.github.util.StatusBarUtil;

/**
 * @author sunfusheng on 2018/7/26.
 */
public class UserActivity extends BaseActivity {

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
        fragmentTransaction.add(R.id.container, UserFragment.instance(getIntent().getStringExtra(Constants.Bundle.USERNAME)));
        fragmentTransaction.commitAllowingStateLoss();
    }
}
