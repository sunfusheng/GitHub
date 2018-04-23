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

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_repo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Repo item) {
        holder.vName.setText(item.getName());

        if (TextUtils.isEmpty(item.getDescription())) {
            holder.vDesc.setVisibility(View.GONE);
        } else {
            holder.vDesc.setVisibility(View.VISIBLE);
            holder.vDesc.setText(item.getDescription());
        }

        holder.vLanguage.setText(item.getLanguage());
        if (item.getStargazers_count() <= 0) {
            holder.vStarCountImg.setVisibility(View.GONE);
            holder.vStarCount.setVisibility(View.GONE);
        } else {
            holder.vStarCountImg.setVisibility(View.VISIBLE);
            holder.vStarCount.setVisibility(View.VISIBLE);
            holder.vStarCount.setText(String.valueOf(item.getStargazers_count()));
        }
        holder.vTime.setText(DateUtil.convertString2String(item.getPushed_at()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vName;
        TextView vDesc;
        TextView vLanguage;
        ImageView vStarCountImg;
        TextView vStarCount;
        TextView vTime;

        ViewHolder(View view) {
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
