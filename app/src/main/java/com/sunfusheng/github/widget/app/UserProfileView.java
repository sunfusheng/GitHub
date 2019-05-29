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
import com.sunfusheng.github.viewmodel.UserDetailViewModel;
import com.sunfusheng.github.viewmodel.base.VM;
import com.sunfusheng.github.viewmodel.base.VMProviders;
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
    private TextView vTip;

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
        vTip = findViewById(R.id.tip);

        vRepoLayout = findViewById(R.id.repo_layout);
        vRepoCount = findViewById(R.id.repo_count);
        vFollowersLayout = findViewById(R.id.followers_layout);
        vFollowersCount = findViewById(R.id.followers_count);
        vFollowingLayout = findViewById(R.id.following_layout);
        vFollowingCount = findViewById(R.id.following_count);

        vTip.setVisibility(VISIBLE);
        vTip.setText("Loading...");
        vBio.setVisibility(VISIBLE);
        vBio.setText("BIO: Loading...");
    }

    public void setUsername(String username) {
        UserDetailViewModel viewModel = VMProviders.of(getContext(), UserDetailViewModel.class);
        viewModel.liveData.observe(VM.getActivity(getContext()), it -> {
            if (it.loadingState == LoadingState.SUCCESS) {
                setUser(it.data);
            }
        });
        viewModel.request(username, FetchMode.DEFAULT);
    }

    public void setLoadingState(@LoadingState int loadingState) {
        switch (loadingState) {
            case LoadingState.LOADING:
                break;
            case LoadingState.SUCCESS:
                break;
            case LoadingState.ERROR:
                vTip.setText("Load Failed!");
                vBio.setText("BIO: Load Failed!");
                break;
            case LoadingState.EMPTY:
                vTip.setText("Empty!");
                vBio.setText("BIO: Empty!");
                break;
        }
    }

    public void setUser(User user) {
        vTip.setVisibility(GONE);
        int infoCount = 0;
        vAvatarBg.load(user.avatar_url, R.mipmap.ic_blur_default, new BlurTransformation(getContext(), 20, 20));
        vAvatar.load(user.avatar_url, R.mipmap.ic_default_avatar, 5);

        if (!TextUtils.isEmpty(user.company)) {
            infoCount++;
            vCompanyLayout.setVisibility(VISIBLE);
            vCompany.setText(user.company);
        } else {
            vCompanyLayout.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(user.location)) {
            infoCount++;
            vLocationLayout.setVisibility(VISIBLE);
            vLocation.setText(user.location);
        } else {
            vLocationLayout.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(user.email)) {
            infoCount++;
            vEmailLayout.setVisibility(VISIBLE);
            vEmail.setText(user.email);
        } else {
            vEmailLayout.setVisibility(GONE);
        }

        String blog = user.blog;
        if (TextUtils.isEmpty(blog)) {
            blog = user.html_url;
        }
        if (!TextUtils.isEmpty(blog)) {
            infoCount++;
            vBlogLayout.setVisibility(VISIBLE);
            vBlog.setText(blog);
        } else {
            vBlogLayout.setVisibility(GONE);
        }

        if (infoCount == 0) {
            vTip.setVisibility(VISIBLE);
            vTip.setText("No Information!");
        }

        vBio.setVisibility(VISIBLE);
        vBio.setText(TextUtils.isEmpty(user.bio) ? "No Biography" : "BIO: " + user.bio);

        vRepoCount.setText(String.valueOf(user.public_repos + user.owned_private_repos));
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
