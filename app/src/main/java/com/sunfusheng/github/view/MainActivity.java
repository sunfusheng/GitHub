package com.sunfusheng.github.view;

import android.os.Bundle;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.NavigationManager;
import com.sunfusheng.github.R;
import com.sunfusheng.github.util.PreferenceUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toLoginActivity();
    }

    private void toLoginActivity() {
        if (true || !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.USERNAME) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.PASSWORD) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.AUTH) ||
                !PreferenceUtil.getInstance().contains(Constants.PreferenceKey.TOKEN)) {
            NavigationManager.toLoginActivity();
            finish();
        }
    }

}
