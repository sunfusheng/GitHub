package com.sunfusheng.github.viewmodel.vm;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VM4<T1, T2, T3, T4> extends VM3<T1, T2, T3> {
    private final MutableLiveData<T4> liveData4 = new MutableLiveData<>();

    public void onNotify(@NonNull Activity activity, Observer<T1> observer1, Observer<T2> observer2, Observer<T3> observer3, Observer<T4> observer4) {
        super.onNotify(activity, observer1, observer2, observer3);
        liveData4.observe(getActivity(activity), observer4);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T1> observer1, Observer<T2> observer2, Observer<T3> observer3, Observer<T4> observer4) {
        onNotify(getActivity(fragment.getActivity()), observer1, observer2, observer3, observer4);
    }

    public void setValue4(T4 value) {
        runOnMainThread(() -> liveData4.setValue(value));
    }

    public T4 getValue4() {
        return liveData4.getValue();
    }
}
