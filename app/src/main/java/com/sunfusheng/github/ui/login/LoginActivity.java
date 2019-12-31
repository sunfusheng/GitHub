package com.sunfusheng.github.ui.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.cache.db.UserDatabase;
import com.sunfusheng.github.model.AuthParams;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseActivity;
import com.sunfusheng.github.http.exception.HttpExceptionHandler;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.util.ToastUtil;
import com.sunfusheng.github.widget.SvgView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/10.
 */
public class LoginActivity extends BaseActivity {
    private EditText vUsername;
    private EditText vPassword;
    private TextView vLogin;
    private SvgView svgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initSvgView();
    }

    private void initView() {
        vUsername = findViewById(R.id.username);
        vPassword = findViewById(R.id.password);
        vLogin = findViewById(R.id.login);
        svgView = findViewById(R.id.svg_view);

        String username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME, "");
        if (!TextUtils.isEmpty(username)) {
            vUsername.setText(username);
            vUsername.setSelection(username.length());
        }

        vUsername.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        vUsername.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                vPassword.requestFocus();
                return true;
            }
            return false;
        });

        vPassword.setImeOptions(EditorInfo.IME_ACTION_SEND);
        vPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                login();
                return true;
            }
            return false;
        });

        vLogin.setOnClickListener(v -> login());


    }

    private void initSvgView() {
        svgView.postDelayed(() -> {
            svgView.start();
        }, 200);
    }

    @Override
    protected void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
    }

    @SuppressLint("CheckResult")
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

        showProgressDialog(R.string.com_waiting_login);

        String basicAuth = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        PreferenceUtil.getInstance().put(Constants.PreferenceKey.AUTH, basicAuth);

        Observable.zip(Api.getLoginService().fetchUser(), Api.getLoginService().fetchToken(AuthParams.getParams()),
                (user, auth) -> {
                    if (user == null || TextUtils.isEmpty(user.login) || auth == null || TextUtils.isEmpty(auth.getToken())) {
                        return false;
                    }

                    UserDatabase.instance().getUserDao().insert(user);
                    PreferenceUtil.getInstance().put(Constants.PreferenceKey.USERNAME, user.login);
                    PreferenceUtil.getInstance().put(Constants.PreferenceKey.TOKEN, auth.getToken());
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    dismissProgressDialog();
                    if (!it) {
                        PreferenceUtil.getInstance().remove(Constants.PreferenceKey.TOKEN);
                    } else {
                        NavigationManager.toMainActivity(this);
                        finish();
                    }
                }, throwable -> {
                    dismissProgressDialog();
                    ToastUtil.toast(HttpExceptionHandler.handleException(throwable).toString());
                });
    }
}
