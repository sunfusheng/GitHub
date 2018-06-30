package com.sunfusheng.github.widget.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import static com.sunfusheng.github.widget.multitype.Preconditions.checkNotNull;

/**
 * @author sunfusheng on 2018/6/30.
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private @NonNull List<?> items;
    private @NonNull MultiTypePool typePool;

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

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
