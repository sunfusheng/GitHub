package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/7/14.
 */
public class NonsupportBinder extends ItemViewBinder<Object, NonsupportBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_default_binder, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Object item) {
        String type;
        if (item instanceof Event) {
            type = "【" + ((Event) item).getType() + "】";
        } else {
            type = "【" + item.getClass().getSimpleName() + ".class】";
        }
        holder.vTitle.setText("暂不支持" + type + "类型数据");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vTitle;

        ViewHolder(View view) {
            super(view);
            vTitle = view.findViewById(R.id.title);
        }
    }
}
