package com.sunfusheng.github.viewmodel.base;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VM2<T1, T2> extends VM<T1> {
    private final MutableLiveData<T2> liveData2 = new MutableLiveData<>();

    public void onNotify(@NonNull Activity activity, Observer<T1> observer1, Observer<T2> observer2) {
        super.onNotify(activity, observer1);
        liveData2.observe(getActivity(activity), observer2);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T1> observer1, Observer<T2> observer2) {
        onNotify(getActivity(fragment.getActivity()), observer1, observer2);
    }

    public void setValue2(T2 value) {
        runOnMainThread(() -> liveData2.setValue(value));
    }

    public T2 getValue2() {
        return liveData2.getValue();
    }
}
