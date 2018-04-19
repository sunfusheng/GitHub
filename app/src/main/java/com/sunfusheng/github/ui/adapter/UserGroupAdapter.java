package com.sunfusheng.github.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.sunfusheng.GroupRecyclerViewAdapter;
import com.sunfusheng.GroupViewHolder;

/**
 * @author sunfusheng on 2018/4/19.
 */
public class UserGroupAdapter extends GroupRecyclerViewAdapter {

    public UserGroupAdapter(Context context) {
        super(context);
    }

    @Override
    public int getHeaderLayoutId(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayoutId(int viewType) {
        return 0;
    }

    @Override
    public int getFooterLayoutId(int viewType) {
        return 0;
    }

    @Override
    public void onBindHeaderViewHolder(GroupViewHolder holder, Object item, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(GroupViewHolder holder, Object item, int groupPosition, int childPosition) {

    }

    @Override
    public void onBindFooterViewHolder(GroupViewHolder holder, Object item, int groupPosition) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
