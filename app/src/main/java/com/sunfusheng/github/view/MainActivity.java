package com.sunfusheng.github.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.NavigationManager;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.annotation.LoadingState;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.glideimageview.GlideImageView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private GlideImageView vUserAvatar;
    private TextView vUserName;

    long start;
    long end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toLoginActivity();

        setContentView(R.layout.activity_main);

        if (true) {
            Observable<String> local = local(1000);
            Observable<String> remote = remote(1000);

            Observable.merge(local, remote)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            start = System.currentTimeMillis();
                            Log.d("--->", "onSubscribe()");
                        }

                        @Override
                        public void onNext(String s) {
                            end = System.currentTimeMillis();
                            Log.d("--->", "onNext() " + s + " : " + (end - start));
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("--->", "onError()");
                        }

                        @Override
                        public void onComplete() {
                            end = System.currentTimeMillis();
                            Log.d("--->", "onComplete()" + " : " + (end - start));
                        }
                    });

//            remote(2000)
//                    .publish(remote -> Observable.merge(local, remote).takeUntil(remote))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<String>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            start = System.currentTimeMillis();
//                        }
//
//                        @Override
//                        public void onNext(String s) {
//                            end = System.currentTimeMillis();
//                            Log.d("--->", s + " : " + (end - start));
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
            return;
        }

        vUserAvatar = findViewById(R.id.user_avatar);
        vUserName = findViewById(R.id.username);

        String username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);

        ViewModelProviders.of(this).get(UserViewModel.class)
                .getUser(username, FetchMode.DEFAULT)
                .observe(this, it -> {
                    if (it == null) return;
                    switch (it.loadingState) {
                        case LoadingState.LOADING:
                            showProgressDialog();
                            break;
                        case LoadingState.SUCCESS:
                            dismissProgressDialog();
                            vUserAvatar.loadImage(it.data.getAvatar_url(), R.mipmap.ic_launcher);
                            vUserName.setText(it.data.getName());
                            break;
                        case LoadingState.ERROR:
                            dismissProgressDialog();
                            vUserName.setText(it.errorString());
                            break;
                        case LoadingState.EMPTY:
                            dismissProgressDialog();
                            vUserName.setText(it.msg);
                            break;
                    }
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

    public Observable<String> local(long ms) {
        return Observable.defer(() -> Observable.just("local"))
                .doOnNext(s -> {
                    end = System.currentTimeMillis();
                    Log.d("--->", "doOnNext() : " + s + " : " + (end - start));
                })
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    SystemClock.sleep(ms);
                    return s + " sleep:" + ms;
                });
    }

    public Observable<String> remote(long ms) {
        return Observable.defer(() -> Observable.just("remote"))
                .doOnNext(s -> {
                    end = System.currentTimeMillis();
                    Log.d("--->", "doOnNext() : " + s + " : " + (end - start));
                })
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    SystemClock.sleep(ms);
                    return s + " sleep:" + ms;
                });
    }

}
