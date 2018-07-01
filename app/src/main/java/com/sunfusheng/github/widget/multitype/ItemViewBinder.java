package com.sunfusheng.github.widget.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author sunfusheng on 2018/6/30.
 */
public abstract class ItemViewBinder<T, VH extends ViewHolder> {

//    protected @NonNull Context context;
//    protected @NonNull T item;
//
//    public ItemViewBinder(@NonNull Context context, @NonNull T item) {
//        this.context = context;
//        this.item = item;
//    }

    protected abstract @NonNull VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item);

    protected void onBindViewHolder(@NonNull VH holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    protected long getItemId(@NonNull T item) {
        return RecyclerView.NO_ID;
    }
}
