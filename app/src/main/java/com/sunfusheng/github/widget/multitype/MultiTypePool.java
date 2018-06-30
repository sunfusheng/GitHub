package com.sunfusheng.github.widget.multitype;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Function;

import static com.sunfusheng.github.widget.multitype.Preconditions.checkNotNull;

/**
 * @author sunfusheng on 2018/6/30.
 */
public class MultiTypePool {

    private Map<Class, MultiType> typeMap = new HashMap<>();

    public <T, K> void register(@NonNull Class<? extends T> clazz, Function<T, K> keyGenerator, K key, @NonNull ItemViewBinder<T, ?> binder) {
        checkNotNull(clazz);
        checkNotNull(binder);
        MultiType multiType = typeMap.get(clazz);
        if (multiType == null) {
            multiType = new MultiType<T, K>();
            multiType.keyGenerator = keyGenerator;
            typeMap.put(clazz, multiType);
        }
        multiType.register(key, binder);
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull ItemViewBinder<T, ?> binder) {
        checkNotNull(clazz);
        checkNotNull(binder);
        MultiType multiType = typeMap.get(clazz);
        if (multiType == null) {
            multiType = new MultiType<T, Void>();
            typeMap.put(clazz, multiType);
        }
        multiType.register(binder);
    }

    public @NonNull
    ItemViewBinder<?, ?> getItemViewBinder(@NonNull Object model) {
        MultiType multiType = typeMap.get(model.getClass());
        if (multiType == null) {
            throw new BinderNotFoundException(model.getClass());
        }
        return multiType.getItemViewBinder(model);
    }
}
