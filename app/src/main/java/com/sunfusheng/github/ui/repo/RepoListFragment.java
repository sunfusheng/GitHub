package com.sunfusheng.github.ui.repo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.github.viewmodel.RepoListViewModel;
import com.sunfusheng.github.viewmodel.vm.VMProviders;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableHelper;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

/**
 * @author sunfusheng on 2018/7/25.
 */
public class RepoListFragment extends BaseFragment implements ScrollableHelper.ScrollableViewContainer {

    private RecyclerViewWrapper recyclerViewWrapper;
    private String username;

    public static RepoListFragment newFragment(String username) {
        RepoListFragment fragment = new RepoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle arguments) {
        if (arguments != null) {
            username = arguments.getString(Constants.Bundle.USERNAME);
        }
    }

    @Override
    public int inflateLayout() {
        return R.layout.layout_recyclerview_wrapper;
    }

    @Override
    public void initView(@NonNull View rootView) {
        initRecyclerViewWrapper();
        fetchRepoList();
    }

    private void initRecyclerViewWrapper() {
        recyclerViewWrapper = vRootView.findViewById(R.id.recyclerViewWrapper);
        recyclerViewWrapper.setLoadingLayout(R.layout.layout_loading_for_scrollable);
        recyclerViewWrapper.setEmptyLayout(R.layout.layout_empty_for_scrollable);

        recyclerViewWrapper.enableRefresh(false);
        recyclerViewWrapper.enableLoadMore(false);

        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(false);
        repoBinder.showExactNum(true);
        repoBinder.showUpdateTime(true);
        recyclerViewWrapper.register(Repo.class, repoBinder);
    }

    private void fetchRepoList() {
        RepoListViewModel viewModel = VMProviders.of(this, RepoListViewModel.class);
        viewModel.liveData.observe(this, it -> {
            Log.d("sfs", "fetchRepoList() loadingState: " + it.loadingStateString + " fetchMode: " + it.fetchModeString);

            switch (it.loadingState) {
                case LoadingState.SUCCESS:
                    recyclerViewWrapper.setLoadingState(it.loadingState);
                    recyclerViewWrapper.setItems(it.data);
                    break;
                case LoadingState.ERROR:
                case LoadingState.EMPTY:
                    recyclerViewWrapper.setLoadingState(it.loadingState);
                    break;
            }
        });
        viewModel.doRequest(username, 1, FetchMode.REMOTE);
    }

    @Override
    public View getScrollableView() {
        return recyclerViewWrapper.getRecyclerView();
    }
}
