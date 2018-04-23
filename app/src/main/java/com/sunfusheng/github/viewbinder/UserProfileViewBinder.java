package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.glideimageview.GlideImageView;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class UserProfileViewBinder extends ItemViewBinder<User, UserProfileViewBinder.ViewHolder> {

    private String username;

    public UserProfileViewBinder() {
        this.username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_user_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull User item) {
        holder.vAvatar.loadImage(item.getAvatar_url(), R.color.background_common);
        holder.vInfo.setText("签名: " + item.getBio() + "\n" +
                "公司: " + item.getCompany() + "\n" +
                "位置: " + item.getLocation() + "\n" +
                "博客: " + item.getBlog() + "\n" +
                "地址: " + item.getHtml_url());

        holder.vRepoCount.setText(String.valueOf(item.getPublic_repos()));
        holder.vFollowingCount.setText(String.valueOf(item.getFollowing()));
        holder.vFollowersCount.setText(String.valueOf(item.getFollowers()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        GlideImageView vAvatar;
        TextView vInfo;
        LinearLayout vRepo;
        TextView vRepoCount;
        LinearLayout vFollowing;
        TextView vFollowingCount;
        LinearLayout vFollowers;
        TextView vFollowersCount;

        ViewHolder(View view) {
            super(view);
            vAvatar = view.findViewById(R.id.avatar);
            vInfo = view.findViewById(R.id.info);
            vRepo = view.findViewById(R.id.repo);
            vRepoCount = view.findViewById(R.id.repo_count);
            vFollowing = view.findViewById(R.id.following);
            vFollowingCount = view.findViewById(R.id.following_count);
            vFollowers = view.findViewById(R.id.followers);
            vFollowersCount = view.findViewById(R.id.followers_count);
        }
    }
}
