package com.sunfusheng.github.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Contribution;
import com.sunfusheng.github.widget.ContributionsView;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class ContributionsViewBinder extends ItemViewBinder<Contribution, ContributionsViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_contributions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Contribution item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vTitle;
        ContributionsView vContributions;

        ViewHolder(View view) {
            super(view);
            vTitle = view.findViewById(R.id.title);
            vContributions = view.findViewById(R.id.contributions);
        }
    }
}
