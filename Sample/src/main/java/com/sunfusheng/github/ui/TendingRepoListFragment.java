package com.sunfusheng.github.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.R;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.net.Api;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeTendingData();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        if (getView() == null) return;
        toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Tending");
    }

    private void initView() {
        View view = getView();
        if (view == null) return;
        recyclerViewWrapper = view.findViewById(R.id.recyclerViewWrapper);

        recyclerViewWrapper.enableRefresh(false);
        recyclerViewWrapper.enableLoadMore(false);

        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(true);
        recyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @SuppressLint("CheckResult")
    private void observeTendingData() {
        Api.getWebPageService().fetchTendingRepos("Daily")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    HtmlUtil.parseTrendingPageData(it.string(), repos -> {
                        recyclerViewWrapper.setItems(repos);
                    }, Throwable::printStackTrace);
                }, Throwable::printStackTrace);
    }

}
