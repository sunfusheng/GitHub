package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.widget.app.RepoInfoView;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/7/14.
 */
public class WatchForkEventBinder extends ItemViewBinder<Event, WatchForkEventBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_watch_fork_event, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Event item) {
        holder.vAvatar.load(item.actor.avatar_url, R.mipmap.ic_avatar_default1, 3);
        holder.vEventDesc.setText(Utils.getEventDesc(item));
        holder.vRepoName.setText(item.repo.full_name);
        if (!TextUtils.isEmpty(item.repo.description)) {
            holder.vRepoDesc.setVisibility(View.VISIBLE);
            holder.vRepoDesc.setText(item.repo.description);
        } else {
            holder.vRepoDesc.setVisibility(View.GONE);
        }
        holder.vRepoInfo.setData(item.repo);

        holder.rlRepo.setOnClickListener(v -> {

        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        GlideImageView vAvatar;
        TextView vEventDesc;
        RelativeLayout rlRepo;
        TextView vRepoName;
        TextView vRepoDesc;
        RepoInfoView vRepoInfo;

        ViewHolder(View view) {
            super(view);
            vAvatar = view.findViewById(R.id.avatar);
            vEventDesc = view.findViewById(R.id.event_desc);
            rlRepo = view.findViewById(R.id.rl_repo);
            vRepoName = view.findViewById(R.id.repo_name);
            vRepoDesc = view.findViewById(R.id.repo_desc);
            vRepoInfo = view.findViewById(R.id.repo_info);
        }
    }
}
