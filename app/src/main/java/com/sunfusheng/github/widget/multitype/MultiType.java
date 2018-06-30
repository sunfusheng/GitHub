package com.sunfusheng.github.widget.multitype;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Function;

/**
 * @author sunfusheng on 2018/6/30.
 */
public class MultiType<T, K> {

    private Map<Object, ItemViewBinder<?, ?>> binderMap = new HashMap<>();
    protected Function<T, K> keyGenerator;

    public @NonNull ItemViewBinder<?, ?> getItemViewBinder(@NonNull Object model) {
        try {
            T concreteModel = (T) model;
            if (keyGenerator == null) {
                binderMap.get(null);
            } else {
                K key = keyGenerator.apply(concreteModel);
                if (key != null && binderMap.containsKey(key)) {
                    return binderMap.get(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BinderNotFoundException(model.getClass());
    }

    public void register(K key, ItemViewBinder<?, ?> binder) {
        binderMap.put(key, binder);
    }

    public void register(ItemViewBinder<?, ?> binder) {
        binderMap.put(null, binder);
    }
}
