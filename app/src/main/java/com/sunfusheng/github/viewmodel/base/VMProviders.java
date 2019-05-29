package com.sunfusheng.github.viewmodel.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VMProviders {

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull Context context, @NonNull Class<T> vmClass) {
        return ViewModelProviders.of(VM.getActivity(context)).get(vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull FragmentActivity activity, @NonNull Class<T> vmClass) {
        return ViewModelProviders.of(activity).get(vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull Fragment fragment, @NonNull Class<T> vmClass) {
        return of(VM.getActivity(fragment.getActivity()), vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T ofFragment(@NonNull Fragment fragment, @NonNull Class<T> vmClass) {
        return ViewModelProviders.of(fragment).get(vmClass);
    }
}
