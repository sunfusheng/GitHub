package com.sunfusheng.github.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VM<T> extends ViewModel {
    private final MutableLiveData<T> liveData = new MutableLiveData();

    public void put(T data) {
        VmUtil.setValue(() -> {
            liveData.setValue(data);
        });
    }

    public void onNotify(@NonNull Activity activity, Observer<T> observer) {
        liveData.observe(VmUtil.getActivity(activity), observer);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T> observer) {
        onNotify(VmUtil.getActivity(fragment.getActivity()), observer);
    }

    public T getData1() {
        return liveData.getValue();
    }
}
