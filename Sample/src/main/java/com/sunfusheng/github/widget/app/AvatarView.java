package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.util.AttributeSet;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.NavigationManager;

/**
 * @author sunfusheng on 2018/7/28.
 */
public class AvatarView extends GlideImageView {
    private String username;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnClickListener(v -> {
            NavigationManager.toUserActivity(getContext(), username);
        });
    }

    public void loadAvatar(String username, String avatarUrl) {
        this.username = username;
        load(avatarUrl, R.mipmap.ic_default_avatar, 3);
    }
}
