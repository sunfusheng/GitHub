package com.sunfusheng.github.ui.discover;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunfusheng.github.R;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.HtmlUtil;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class TrendRepoListFragment extends BaseFragment {

    private RecyclerViewWrapper vRecyclerViewWrapper;

    private String mSince = "daily";

    public static TrendRepoListFragment newFragment(String since) {
        TrendRepoListFragment fragment = new TrendRepoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("since", since);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle arguments) {
        if (arguments != null) {
            mSince = arguments.getString("since");
        }
    }

    @Override
    public int inflateLayout() {
        return R.layout.layout_recyclerview_wrapper;
    }

    @Override
    public void initView(@NonNull View rootView) {
        vRecyclerViewWrapper = rootView.findViewById(R.id.recyclerViewWrapper);

        initRecyclerViewWrapper();
        observeTendingData();
    }

    private void initRecyclerViewWrapper() {
        vRecyclerViewWrapper.enableRefresh(false);
        vRecyclerViewWrapper.enableLoadMore(false);

        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(true);
        repoBinder.showExactNum(false);
        vRecyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @SuppressLint("CheckResult")
    private void observeTendingData() {
        Api.getWebPageService().fetchTendingRepos(mSince)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    HtmlUtil.parseTrendingPageByHtmlText(it.string(), repos -> {
                        vRecyclerViewWrapper.setItems(repos);
                    }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

}
