package com.sunfusheng.github.viewmodel.vm;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * @author sunfusheng on 2018/7/6.
 */
public class VM3<T1, T2, T3> extends VM2<T1, T2> {
    private final MutableLiveData<T3> liveData3 = new MutableLiveData<>();

    public void onNotify(@NonNull Activity activity, Observer<T1> observer1, Observer<T2> observer2, Observer<T3> observer3) {
        super.onNotify(activity, observer1, observer2);
        liveData3.observe(getActivity(activity), observer3);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T1> observer1, Observer<T2> observer2, Observer<T3> observer3) {
        onNotify(getActivity(fragment.getActivity()), observer1, observer2, observer3);
    }

    public void setValue3(T3 value) {
        runOnMainThread(() -> liveData3.setValue(value));
    }

    public T3 getValue3() {
        return liveData3.getValue();
    }
}
