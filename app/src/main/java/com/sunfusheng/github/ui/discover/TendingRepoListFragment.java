package com.sunfusheng.github.ui.discover;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.HtmlUtil;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class TendingRepoListFragment extends BaseFragment {

    private RecyclerViewWrapper recyclerViewWrapper;

    private String since = "daily";

    public static TendingRepoListFragment newFragment(String since) {
        TendingRepoListFragment fragment = new TendingRepoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("since", since);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle arguments) {
        if (arguments != null) {
            since = arguments.getString("since");
        }
    }

    @Override
    public int inflateLayout() {
        return R.layout.layout_recyclerview_wrapper;
    }

    @Override
    public void initView(@NonNull View rootView) {
        recyclerViewWrapper = rootView.findViewById(R.id.recyclerViewWrapper);

        initRecyclerViewWrapper();
        observeTendingData();
    }

    private void initRecyclerViewWrapper() {
        recyclerViewWrapper.enableRefresh(false);
        recyclerViewWrapper.enableLoadMore(false);

        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(true);
        repoBinder.showExactNum(false);
        recyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @SuppressLint("CheckResult")
    private void observeTendingData() {
        Api.getWebPageService().fetchTendingRepos(since)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    HtmlUtil.parseTrendingPageByHtmlText(it.string(), repos -> {
                        recyclerViewWrapper.setItems(repos);
                    }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

}
