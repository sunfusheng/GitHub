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
import com.sunfusheng.github.util.DateUtil;
import com.sunfusheng.github.util.Utils;

/**
 * @author sunfusheng on 2018/7/15.
 */
public class RepoInfo extends LinearLayout {
    TextView vLanguage;
    ImageView vStarCountImg;
    TextView vStarCount;
    TextView vTime;

    public RepoInfo(Context context) {
        this(context, null);
    }

    public RepoInfo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepoInfo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_repo_info, this);
        vLanguage = findViewById(R.id.language);
        vStarCountImg = findViewById(R.id.star_count_img);
        vStarCount = findViewById(R.id.star_count);
        vTime = findViewById(R.id.time);
    }

    public void setData(Repo repo) {
        if (!TextUtils.isEmpty(repo.language)) {
            vLanguage.setVisibility(VISIBLE);
            vLanguage.setText(repo.language);
            vLanguage.setTextColor(Utils.getColorByLanguage(repo.language));
        } else {
            vLanguage.setVisibility(GONE);
        }

        if (repo.stargazers_count > 0) {
            vStarCountImg.setVisibility(View.VISIBLE);
            vStarCount.setVisibility(View.VISIBLE);
            vStarCount.setText(String.valueOf(repo.stargazers_count));
        } else {
            vStarCountImg.setVisibility(View.GONE);
            vStarCount.setVisibility(View.GONE);
        }

        vTime.setText("Updated " + DateUtil.convertString2String(repo.updated_at));
    }
}
