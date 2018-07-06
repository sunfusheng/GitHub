package com.sunfusheng.github.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author sunfusheng on 2018/4/19.
 */
public class VmUtil {
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull FragmentActivity activity, @NonNull Class<T> vmClass) {
        return ViewModelProviders.of(activity).get(vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull Fragment fragment, @NonNull Class<T> vmClass) {
        return of(getActivity(fragment.getActivity()), vmClass);
    }

    static FragmentActivity getActivity(Activity activity) {
        if (activity instanceof FragmentActivity) {
            return (FragmentActivity) activity;
        }
        throw new IllegalArgumentException("activity is not FragmentActivity!");
    }

    static void setValue(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }
}
