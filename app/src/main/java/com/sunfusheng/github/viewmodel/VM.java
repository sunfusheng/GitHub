package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author sunfusheng on 2018/4/19.
 */
public class VM {

    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull FragmentActivity activity, @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(activity).get(modelClass);
    }


    @NonNull
    @MainThread
    public static <T extends ViewModel> T of(@NonNull Fragment fragment, @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(fragment).get(modelClass);
    }
}
