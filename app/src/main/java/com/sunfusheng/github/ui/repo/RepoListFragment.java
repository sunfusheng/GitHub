package com.sunfusheng.github.ui.repo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
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
public class RepoListFragment extends BaseFragment implements ScrollableHelper.ScrollableViewContainer,
        RecyclerViewWrapper.OnRefreshListener,
        RecyclerViewWrapper.OnLoadMoreListener {

    private RecyclerViewWrapper vRecyclerViewWrapper;

    private String mUsername;
    private RepoListViewModel mViewModel;

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
            mUsername = arguments.getString(Constants.Bundle.USERNAME);
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
        vRecyclerViewWrapper = vRootView.findViewById(R.id.recyclerViewWrapper);
        vRecyclerViewWrapper.setLoadingLayout(R.layout.layout_loading_for_scrollable);
        vRecyclerViewWrapper.setEmptyLayout(R.layout.layout_empty_for_scrollable);

        vRecyclerViewWrapper.enableRefresh(true);
        vRecyclerViewWrapper.enableLoadMore(true);

        vRecyclerViewWrapper.setOnRefreshListener(this);
        vRecyclerViewWrapper.setOnLoadMoreListener(this);

        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(false);
        repoBinder.showExactNum(true);
        repoBinder.showUpdateTime(true);
        vRecyclerViewWrapper.register(Repo.class, repoBinder);
    }

    private void fetchRepoList() {
        mViewModel = VMProviders.of(this, RepoListViewModel.class);
        mViewModel.liveData.observe(this, it -> {
            Log.d("sfs", "fetchRepoList() loadingState: " + it.loadingStateString + " fetchMode: " + it.fetchModeString);

            switch (it.loadingState) {
                case LoadingState.SUCCESS:
                    vRecyclerViewWrapper.setLoadingState(it.loadingState);
                    vRecyclerViewWrapper.setItems(it.data);
                    break;
                case LoadingState.ERROR:
                case LoadingState.EMPTY:
                    vRecyclerViewWrapper.setLoadingState(it.loadingState);
                    break;
            }
        });
        mViewModel.username = mUsername;
        mViewModel.load();
    }

    @Override
    public void onRefresh() {
        mViewModel.refresh();
    }

    @Override
    public void onLoadMore() {
        mViewModel.loadMore();
    }

    @Override
    public View getScrollableView() {
        return vRecyclerViewWrapper.getRecyclerView();
    }
}
