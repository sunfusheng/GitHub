package com.sunfusheng.github.widget.multitype;

import android.support.annotation.NonNull;

/**
 * @author sunfusheng on 2018/7/1.
 */
public class BinderNotFoundException extends RuntimeException {

    BinderNotFoundException(@NonNull Class<?> clazz) {
        super("Do you have registered {className}.class to the binder in the adapter/pool?"
                .replace("{className}", clazz.getSimpleName()));
    }
}
