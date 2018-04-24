package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.VM;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class HomeFragment extends BaseFragment {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private String username;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }

        userViewModel = VM.of(this, UserViewModel.class);
        userViewModel.setRequestParams(username, FetchMode.DEFAULT);

        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);

        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    long start;
    long end;

    private void initView() {

        Observable<String> localObservable = local(2000);
        Observable<String> remoteObservable = remote(2000);

        remoteObservable.publish(remote -> Observable.merge(remote, localObservable.takeUntil(remote)))
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
    }

    public Observable<String> local(long ms) {
        return Observable.defer(() -> Observable.just("local"))
                .doOnNext(s -> {
                    end = System.currentTimeMillis();
                    Log.d("--->", "doOnNext() " + s + " : " + (end - start));
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
                    Log.d("--->", "doOnNext() " + s + " : " + (end - start));
                })
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    SystemClock.sleep(ms);
                    return s + " sleep:" + ms;
                });
    }

}
