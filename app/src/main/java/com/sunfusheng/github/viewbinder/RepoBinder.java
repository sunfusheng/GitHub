package com.sunfusheng.github.viewbinder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.widget.app.RepoInfoView;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class RepoBinder extends ItemViewBinder<Repo, RepoBinder.ViewHolder> {

    private boolean showFullName;
    private boolean showExactNum;
    private boolean showUpdateTime;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_repo, parent, false));
    }

    public void showFullName(boolean show) {
        showFullName = show;
    }

    public void showExactNum(boolean show) {
        this.showExactNum = show;
    }

    public void showUpdateTime(boolean show) {
        this.showUpdateTime = show;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Repo item) {
        holder.vName.setText(showFullName ? item.full_name : item.name);

        if (!TextUtils.isEmpty(item.description)) {
            holder.vDesc.setVisibility(View.VISIBLE);
            holder.vDesc.setText(item.description);
        } else {
            holder.vDesc.setVisibility(View.GONE);
        }
        holder.vRepoInfo.showExactNum(showExactNum);
        holder.vRepoInfo.showUpdateTime(showUpdateTime);
        holder.vRepoInfo.setData(item);

        holder.itemView.setOnClickListener(v -> {
            NavigationManager.toRepoDetailActivity(holder.itemView.getContext(), item.full_name);
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
