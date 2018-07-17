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
    TextView vTime;

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
        vTime = findViewById(R.id.time);
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
            vStarCount.setText(Utils.getStarCount(repo.stargazers_count));
        } else {
            vStarCountImg.setVisibility(View.GONE);
            vStarCount.setVisibility(View.GONE);
        }

        vTime.setText("Updated " + Utils.getDateAgo(repo.pushed_at));
    }

}
