package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.User;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.base.VM;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.transformation.BlurTransformation;

/**
 * @author sunfusheng on 2018/7/26.
 */
public class UserProfileView extends LinearLayout {
    private GlideImageView vAvatarBg;
    private GlideImageView vAvatar;

    private LinearLayout vCompanyLayout;
    private TextView vCompany;
    private LinearLayout vLocationLayout;
    private TextView vLocation;
    private LinearLayout vEmailLayout;
    private TextView vEmail;
    private LinearLayout vBlogLayout;
    private TextView vBlog;

    private TextView vBio;

    private LinearLayout vRepoLayout;
    private TextView vRepoCount;
    private LinearLayout vFollowersLayout;
    private TextView vFollowersCount;
    private LinearLayout vFollowingLayout;
    private TextView vFollowingCount;

    public UserProfileView(Context context) {
        this(context, null);
    }

    public UserProfileView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserProfileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_user_profile, this);
        vAvatarBg = findViewById(R.id.avatar_bg);
        vAvatar = findViewById(R.id.avatar);

        vCompanyLayout = findViewById(R.id.company_layout);
        vCompany = findViewById(R.id.company);
        vLocationLayout = findViewById(R.id.location_layout);
        vLocation = findViewById(R.id.location);
        vEmailLayout = findViewById(R.id.email_layout);
        vEmail = findViewById(R.id.email);
        vBlogLayout = findViewById(R.id.blog_layout);
        vBlog = findViewById(R.id.blog);

        vBio = findViewById(R.id.bio);

        vRepoLayout = findViewById(R.id.repo_layout);
        vRepoCount = findViewById(R.id.repo_count);
        vFollowersLayout = findViewById(R.id.followers_layout);
        vFollowersCount = findViewById(R.id.followers_count);
        vFollowingLayout = findViewById(R.id.following_layout);
        vFollowingCount = findViewById(R.id.following_count);
    }

    public void setUsername(String username) {
        UserViewModel viewModel = VmProvider.of(getContext(), UserViewModel.class);
        viewModel.setRequestParams(username, FetchMode.DEFAULT);

        viewModel.liveData.observe(VM.getActivity(getContext()), it -> {
            if (it == null) return;
            if (it.loadingState == LoadingState.SUCCESS) {
                setUser(it.data);
            }
        });
    }

    public void setUser(User user) {
        vAvatarBg.load(user.avatar_url, R.mipmap.ic_blur_default, new BlurTransformation(getContext(), 20, 20));
        vAvatar.load(user.avatar_url, R.mipmap.ic_default_avatar, 5);

        if (!TextUtils.isEmpty(user.company)) {
            vCompanyLayout.setVisibility(VISIBLE);
            vCompany.setText(user.company);
        } else {
            vCompanyLayout.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(user.location)) {
            vLocationLayout.setVisibility(VISIBLE);
            vLocation.setText(user.location);
        } else {
            vLocationLayout.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(user.email)) {
            vEmailLayout.setVisibility(VISIBLE);
            vEmail.setText(user.email);
        } else {
            vEmailLayout.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(user.blog)) {
            vBlogLayout.setVisibility(VISIBLE);
            vBlog.setText(user.blog);
        } else {
            vBlogLayout.setVisibility(GONE);
        }

        vBio.setText(TextUtils.isEmpty(user.bio) ? "暂无简介" : "BIO: " + user.bio);

        vRepoCount.setText(String.valueOf(user.public_repos));
        vFollowingCount.setText(String.valueOf(user.following));
        vFollowersCount.setText(String.valueOf(user.followers));
    }

    public void setRepoClickListener(OnClickListener listener) {
        vRepoLayout.setOnClickListener(listener);
    }

    public void setFollowersClickListener(OnClickListener listener) {
        vFollowersLayout.setOnClickListener(listener);
    }

    public void setFollowingClickListener(OnClickListener listener) {
        vFollowingLayout.setOnClickListener(listener);
    }
}
