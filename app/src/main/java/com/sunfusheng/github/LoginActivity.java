package com.sunfusheng.github;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;

import com.sunfusheng.github.database.UserDatabase;
import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.net.Api;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.util.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/10.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText vUsername;
    private EditText vPassword;
    private TextView vLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.fullScreen(getWindow());
        initView();
    }

    private void initView() {
        vUsername = findViewById(R.id.username);
        vPassword = findViewById(R.id.password);
        vLogin = findViewById(R.id.login);
        vLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = vUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.toast(R.string.username_input);
            return;
        }

        String password = vPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.toast(R.string.password_input);
            return;
        }

        String credentials = username + ":" + password;
        String basicAuth = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.USERNAME, username);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.PASSWORD, password);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.AUTH, basicAuth);

        Observable.zip(Api.getService().login(basicAuth), Api.getService().createAuth(AuthParams.getParams()),
                (user, auth) -> {
                    if (user == null || auth == null) {
                        return false;
                    }
                    UserDatabase.getDefault(this).getUserDao().insert(user);
                    PreferenceUtil.getInstance().put(Constants.PreferenceKey.TOKEN, auth.getToken());
                    return true;
                })
                .onErrorResumeNext(throwable -> {
                    return Observable.just(false);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    if (!it) {
                        PreferenceUtil.getInstance().remove(Constants.PreferenceKey.TOKEN);
                    }
                    ToastUtil.toast(it ? "登录成功" : "登录失败");
                }, Throwable::printStackTrace);
    }
}
