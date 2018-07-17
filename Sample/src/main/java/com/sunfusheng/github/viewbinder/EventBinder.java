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
 * @author sunfusheng on 2018/5/7.
 */
public class EventBinder extends ItemViewBinder<Event, EventBinder.ViewHolder> {

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.item_event, parent, false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(onCreateView(inflater, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Event item) {
        holder.vTitle.setText(item.type);
        holder.vDesc.setText(item.created_at);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vTitle;
        TextView vDesc;

        public ViewHolder(View view) {
            super(view);
            vTitle = view.findViewById(R.id.title);
            vDesc = view.findViewById(R.id.desc);
        }
    }
}
