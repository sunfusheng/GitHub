package com.sunfusheng.github.view;

import android.os.Bundle;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.NavigationManager;
import com.sunfusheng.github.R;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toLoginActivity();

        setContentView(R.layout.activity_main);

        String username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);

        Api.getLoginService().fetchUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    dismissProgressDialog();
                    ToastUtil.toast("code: " + it.code() + " message: " + it.message());
                }, throwable -> {
                    dismissProgressDialog();
                    throwable.printStackTrace();
                });

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

}
