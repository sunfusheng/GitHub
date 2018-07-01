package com.sunfusheng.github.widget.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import io.reactivex.functions.Function;

import static com.sunfusheng.github.widget.multitype.Preconditions.checkNotNull;

/**
 * @author sunfusheng on 2018/6/30.
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private @NonNull List<?> items;
    private @NonNull MultiTypePool typePool;

    private LayoutInflater inflater;

    public MultiTypeAdapter() {
        this(Collections.emptyList());
    }

    public MultiTypeAdapter(@NonNull List<?> items) {
        this(items, new MultiTypePool());
    }

    public MultiTypeAdapter(@NonNull List<?> items, @NonNull MultiTypePool pool) {
        checkNotNull(items);
        checkNotNull(pool);
        this.items = items;
        this.typePool = pool;
    }

    public <T, K> void register(@NonNull Class<? extends T> clazz, Function<T, K> keyGenerator, K key, @NonNull ItemViewBinder<T, ?> binder) {
        typePool.register(clazz, keyGenerator, key, binder);
    }

    public <T> void register(@NonNull Class<? extends T> clazz, @NonNull ItemViewBinder<T, ?> binder) {
        typePool.register(clazz, binder);
    }

    private ItemViewBinder currBinder;

    @Override
    public final int getItemViewType(int position) {
        Object item = items.get(position);
        currBinder = typePool.getItemViewBinder(item);
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return currBinder.onCreateViewHolder(inflater,parent );
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBindViewHolder(holder, position, Collections.emptyList());
    }

    @Override
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Object item = items.get(position);
        currBinder = typePool.getItemViewBinder(item);
        currBinder.onBindViewHolder(holder, item, payloads);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        Object item = items.get(position);
        currBinder = typePool.getItemViewBinder(item);
        return currBinder.getItemId(item);
    }
}
