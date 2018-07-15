package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.DateUtil;
import com.sunfusheng.github.widget.app.RepoInfo;
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
        holder.vAvatar.load(item.actor.avatar_url, R.color.background_common_dark, 3);
        holder.vEventDesc.setText(getEventDesc(item));
        holder.vRepoName.setText(item.repo.full_name);
        holder.vRepoDesc.setText(item.repo.description);
        holder.vRepoInfo.setData(item.repo);

        holder.rlRepo.setOnClickListener(v -> {

        });
    }

    private String getEventDesc(Event item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.actor.login).append(" ");
        if (item.type.equals(Event.WatchEvent)) {
            sb.append("starred ").append(item.repo.full_name);
        } else {
            sb.append("forked ").append(item.payload.forkee.full_name).append(" from ").append(item.repo.full_name);
        }
        sb.append(" ").append(DateUtil.formatDate(item.created_at));
        return sb.toString();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        GlideImageView vAvatar;
        TextView vEventDesc;
        RelativeLayout rlRepo;
        TextView vRepoName;
        TextView vRepoDesc;
        RepoInfo vRepoInfo;

        public ViewHolder(View view) {
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
