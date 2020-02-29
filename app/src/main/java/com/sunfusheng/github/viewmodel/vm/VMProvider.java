package com.sunfusheng.github.viewmodel.vm;


import android.content.Context;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VMProvider {

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull Context context, @NonNull Class<T> vmClass) {
        return new ViewModelProvider(VM.getActivity(context)).get(vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull FragmentActivity activity, @NonNull Class<T> vmClass) {
        return new ViewModelProvider(activity).get(vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull Fragment fragment, @NonNull Class<T> vmClass) {
        return of(VM.getActivity(fragment.getActivity()), vmClass);
    }

    @NonNull
    @MainThread
    public static <T extends ViewModel> T ofFragment(@NonNull Fragment fragment, @NonNull Class<T> vmClass) {
        return new ViewModelProvider(fragment).get(vmClass);
    }
}
