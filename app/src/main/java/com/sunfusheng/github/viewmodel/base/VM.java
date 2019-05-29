package com.sunfusheng.github.viewmodel.base;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VM<T> extends ViewModel {
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private final MutableLiveData<T> liveData = new MutableLiveData<>();

    public void onNotify(@NonNull Activity activity, Observer<T> observer) {
        liveData.observe(getActivity(activity), observer);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T> observer) {
        onNotify(getActivity(fragment.getActivity()), observer);
    }

    public static FragmentActivity getActivity(Context context) {
        if (context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        }
        throw new IllegalArgumentException("context is not FragmentActivity!");
    }

    static void runOnMainThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mainThreadHandler.post(runnable);
        }
    }

    public void setValue(T data) {
        runOnMainThread(() -> liveData.setValue(data));
    }

    public T getValue() {
        return liveData.getValue();
    }
}
