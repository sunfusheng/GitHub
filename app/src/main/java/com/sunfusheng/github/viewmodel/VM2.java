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
public class VM2<T1, T2> extends ViewModel {
    private final MutableLiveData<T1> liveData1 = new MutableLiveData();
    private final MutableLiveData<T2> liveData2 = new MutableLiveData();

    public void put(T1 data1, T2 data2) {
        VmUtil.setValue(() -> {
            liveData1.setValue(data1);
            liveData2.setValue(data2);
        });
    }

    public void onNotify(@NonNull Activity activity, Observer<T1> observer1, Observer<T2> observer2) {
        liveData1.observe(VmUtil.getActivity(activity), observer1);
        liveData2.observe(VmUtil.getActivity(activity), observer2);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T1> observer1, Observer<T2> observer2) {
        onNotify(VmUtil.getActivity(fragment.getActivity()), observer1, observer2);
    }

    public T1 getData1() {
        return liveData1.getValue();
    }

    public T2 getData2() {
        return liveData2.getValue();
    }
}
