package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.util.Utils;
import com.sunfusheng.github.widget.CircleView;

/**
 * @author sunfusheng on 2018/7/15.
 */
public class RepoInfoView extends LinearLayout {
    CircleView vLanguageCircle;
    TextView vLanguage;
    ImageView vStarCountImg;
    TextView vStarCount;
    ImageView vForkCountImg;
    TextView vForkCount;
    TextView vUpdateTime;
    TextView vTendingDesc;

    private boolean showExactNum = false;
    private boolean showUpdateTime = false;

    public RepoInfoView(Context context) {
        this(context, null);
    }

    public RepoInfoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepoInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_repo_info, this);
        vLanguageCircle = findViewById(R.id.language_circle);
        vLanguage = findViewById(R.id.language);
        vStarCountImg = findViewById(R.id.star_count_img);
        vStarCount = findViewById(R.id.star_count);
        vForkCountImg = findViewById(R.id.fork_count_img);
        vForkCount = findViewById(R.id.fork_count);
        vUpdateTime = findViewById(R.id.update_time);
        vTendingDesc = findViewById(R.id.tending_desc);
    }

    public void setData(Repo repo) {
        if (!TextUtils.isEmpty(repo.language)) {
            vLanguageCircle.setVisibility(VISIBLE);
            vLanguage.setVisibility(VISIBLE);
            vLanguage.setText(repo.language);
            vLanguageCircle.setCircleColor(Utils.getColorByLanguage(repo.language));
        } else {
            vLanguageCircle.setVisibility(GONE);
            vLanguage.setVisibility(GONE);
        }

        if (repo.stargazers_count > 0) {
            vStarCountImg.setVisibility(View.VISIBLE);
            vStarCount.setVisibility(View.VISIBLE);
            vStarCount.setText(Utils.getCountDesc(repo.stargazers_count, showExactNum));
        } else {
            vStarCountImg.setVisibility(View.GONE);
            vStarCount.setVisibility(View.GONE);
        }

        if (repo.forks_count > 0) {
            vForkCountImg.setVisibility(VISIBLE);
            vForkCount.setVisibility(VISIBLE);
            vForkCount.setText(Utils.getCountDesc(repo.forks_count, showExactNum));
        } else {
            vForkCountImg.setVisibility(GONE);
            vForkCount.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(repo.pushed_at) && showUpdateTime) {
            vUpdateTime.setVisibility(VISIBLE);
            vUpdateTime.setText("Updated " + Utils.getDateAgo(repo.pushed_at));
        } else {
            vUpdateTime.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(repo.tendingDesc)) {
            vTendingDesc.setVisibility(VISIBLE);
            vTendingDesc.setText(repo.tendingDesc);
        } else {
            vTendingDesc.setVisibility(GONE);
        }
    }

    public RepoInfoView showExactNum(boolean show) {
        this.showExactNum = show;
        return this;
    }

    public RepoInfoView showUpdateTime(boolean show) {
        this.showUpdateTime = show;
        return this;
    }
}
