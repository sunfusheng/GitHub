package com.sunfusheng.github.viewbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.widget.app.AvatarView;
import com.sunfusheng.multitype.ItemViewBinder;

/**
 * @author sunfusheng
 * @since 2020-01-11
 */
public class UserListItemBinder extends ItemViewBinder<User, UserListItemBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull User user) {
        viewHolder.vAvatar.loadAvatar(user.login, user.avatar_url);
        viewHolder.vUsername.setText(user.login);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AvatarView vAvatar;
        TextView vUsername;

        ViewHolder(View view) {
            super(view);
            vAvatar = view.findViewById(R.id.avatar);
            vUsername = view.findViewById(R.id.username);
        }
    }
}
