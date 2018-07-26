package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.widget.app.RepoInfoView;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class RepoBinder extends ItemViewBinder<Repo, RepoBinder.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Repo item) {
        holder.vName.setText(item.name);
        if (!TextUtils.isEmpty(item.description)) {
            holder.vDesc.setVisibility(View.VISIBLE);
            holder.vDesc.setText(item.description);
        } else {
            holder.vDesc.setVisibility(View.GONE);
        }
        holder.vRepoInfo.setData(item);

        holder.itemView.setOnClickListener(v -> {

        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vDesc;
        RepoInfoView vRepoInfo;

        ViewHolder(View view) {
            super(view);
            vName = view.findViewById(R.id.name);
            vDesc = view.findViewById(R.id.desc);
            vRepoInfo = view.findViewById(R.id.repo_info);
        }
    }
}
