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
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/7/18.
 */
public class IssueCommentEventBinder extends ItemViewBinder<Event, IssueCommentEventBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_issue_comment_event, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Event item) {
        holder.vAvatar.load(item.actor.avatar_url, R.mipmap.ic_default_avatar, 3);
        holder.vCommentDesc.setText(Utils.getCommentDesc(item));
        holder.vCommentBody.setText(item.payload.comment.body);
        holder.vIssueAvatar.load(item.payload.issue.user.avatar_url, R.mipmap.ic_default_avatar, 3);
        holder.vIssueDesc.setText(Utils.getIssueDesc(item));
        holder.vIssueTitle.setText(item.payload.issue.title);

        holder.rlIssue.setOnClickListener(v -> {

        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        GlideImageView vAvatar;
        TextView vCommentDesc;
        TextView vCommentBody;
        RelativeLayout rlIssue;
        GlideImageView vIssueAvatar;
        TextView vIssueDesc;
        TextView vIssueTitle;

        ViewHolder(View view) {
            super(view);
            vAvatar = view.findViewById(R.id.avatar);
            vCommentDesc = view.findViewById(R.id.comment_desc);
            vCommentBody = view.findViewById(R.id.comment_body);
            rlIssue = view.findViewById(R.id.rl_issue);
            vIssueAvatar = view.findViewById(R.id.issue_avatar);
            vIssueDesc = view.findViewById(R.id.issue_desc);
            vIssueTitle = view.findViewById(R.id.issue_title);
        }
    }
}
