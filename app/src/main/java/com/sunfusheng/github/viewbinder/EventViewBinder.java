package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.Utils;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class EventViewBinder extends ItemViewBinder<Event, EventViewBinder.ViewHolder> {

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
        holder.vTitle.setText(Utils.getDesc(item.type, item.payload.ref, item.repo.name));

        if (item.payload != null && item.payload.commit != null && !TextUtils.isEmpty(item.payload.commit.message)) {
            holder.vDesc.setVisibility(View.VISIBLE);
            holder.vDesc.setText(item.payload.commit.message);
        } else {
            holder.vDesc.setVisibility(View.GONE);
        }
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
