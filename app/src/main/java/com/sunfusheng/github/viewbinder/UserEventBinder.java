package com.sunfusheng.github.viewbinder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.widget.span.SpanTouchTextView;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng
 * @since 2020-01-15
 */
public class UserEventBinder extends ItemViewBinder<Event, UserEventBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_user_event, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull Event event) {
        String message = Utils.getEventMessage(event);
        if (TextUtils.isEmpty(message)) {
            viewHolder.vEventMsg.setVisibility(View.GONE);
        } else {
            viewHolder.vEventMsg.setVisibility(View.VISIBLE);
            viewHolder.vEventMsg.setText(message);
        }

        viewHolder.vEventDesc.setText(Utils.getEventDesc(viewHolder.vEventDesc.getContext(), event));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SpanTouchTextView vEventDesc;
        TextView vEventMsg;

        ViewHolder(View view) {
            super(view);
            vEventDesc = view.findViewById(R.id.event_desc);
            vEventMsg = view.findViewById(R.id.event_msg);
        }
    }
}
