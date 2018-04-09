package com.sunfusheng.github;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.sunfusheng.github.database.UserDatabase;
import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private EditText vName;
    private EditText vPassword;
    private TextView vLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        vName = findViewById(R.id.name);
        vPassword = findViewById(R.id.password);
        vLogin = findViewById(R.id.login);
        vLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String userName = vName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.toast("请输入用户名");
            return;
        }

        String password = vPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.toast("请输入密码");
            return;
        }

        String credentials = userName + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.USER_NAME, userName);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.PASSWORD, password);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.AUTH, auth);

        Observable.zip(Api.getService().login(auth), Api.getService().createAuth(AuthParams.getParams()),
                (user, authResponse) -> {
                    if (user == null || authResponse == null) {
                        return false;
                    }
                    Log.d("--->", user.toString());
                    Log.d("--->", authResponse.toString());

                    UserDatabase.getDefault(MainActivity.this).getUserDao().insert(user);
                    PreferenceUtil.getInstance().put(Constants.PreferenceKey.TOKEN, authResponse.getToken());
                    return true;
                })
                .onErrorResumeNext(throwable -> {
                    return Observable.just(false);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    ToastUtil.toast(it ? "登录成功" : "登录失败");
                }, Throwable::printStackTrace);
    }

}
