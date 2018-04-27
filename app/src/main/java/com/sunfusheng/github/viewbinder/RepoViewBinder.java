package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.util.DateUtil;
import com.sunfusheng.github.util.LanguageColorUtil;
import com.sunfusheng.github.util.PreferenceUtil;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/4/23.
 */
public class RepoViewBinder extends ItemViewBinder<Repo, RepoViewBinder.ViewHolder> {

    private String username;

    public RepoViewBinder() {
        this.username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.item_repo, parent, false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(onCreateView(inflater, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Repo item) {
        holder.vName.setText(item.name);

        if (!TextUtils.isEmpty(item.description)) {
            holder.vDesc.setVisibility(View.VISIBLE);
            holder.vDesc.setText(item.description);
        } else {
            holder.vDesc.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.language)) {
            holder.vLanguage.setVisibility(View.VISIBLE);
            holder.vLanguage.setText(item.language);
            holder.vLanguage.setTextColor(LanguageColorUtil.getColor(item.language));
        } else {
            holder.vLanguage.setVisibility(View.GONE);
        }

        if (item.stargazers_count > 0) {
            holder.vStarCountImg.setVisibility(View.VISIBLE);
            holder.vStarCount.setVisibility(View.VISIBLE);
            holder.vStarCount.setText(String.valueOf(item.stargazers_count));
        } else {
            holder.vStarCountImg.setVisibility(View.GONE);
            holder.vStarCount.setVisibility(View.GONE);
        }

        holder.vTime.setText(DateUtil.convertString2String(item.pushed_at));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vDesc;
        TextView vLanguage;
        ImageView vStarCountImg;
        TextView vStarCount;
        TextView vTime;

        public ViewHolder(View view) {
            super(view);
            vName = view.findViewById(R.id.name);
            vDesc = view.findViewById(R.id.desc);
            vLanguage = view.findViewById(R.id.language);
            vStarCountImg = view.findViewById(R.id.star_count_img);
            vStarCount = view.findViewById(R.id.star_count);
            vTime = view.findViewById(R.id.time);
        }
    }
}
