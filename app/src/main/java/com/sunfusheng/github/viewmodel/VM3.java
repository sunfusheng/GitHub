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
public class VM3<T1, T2, T3> extends ViewModel {
    private final MutableLiveData<T1> liveData1 = new MutableLiveData();
    private final MutableLiveData<T2> liveData2 = new MutableLiveData();
    private final MutableLiveData<T3> liveData3 = new MutableLiveData();

    public void put(T1 data1, T2 data2, T3 data3) {
        VmUtil.setValue(() -> {
            liveData1.setValue(data1);
            liveData2.setValue(data2);
            liveData3.setValue(data3);
        });
    }

    public void onNotify(@NonNull Activity activity, Observer<T1> observer1, Observer<T2> observer2, Observer<T3> observer3) {
        liveData1.observe(VmUtil.getActivity(activity), observer1);
        liveData2.observe(VmUtil.getActivity(activity), observer2);
        liveData3.observe(VmUtil.getActivity(activity), observer3);
    }

    public void onNotify(@NonNull Fragment fragment, Observer<T1> observer1, Observer<T2> observer2, Observer<T3> observer3) {
        onNotify(VmUtil.getActivity(fragment.getActivity()), observer1, observer2, observer3);
    }

    public T1 getData1() {
        return liveData1.getValue();
    }

    public T2 getData2() {
        return liveData2.getValue();
    }

    public T3 getData3() {
        return liveData3.getValue();
    }
}
