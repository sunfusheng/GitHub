package com.sunfusheng.github.ui.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunfusheng.github.R;
import com.sunfusheng.github.datasource.DataSourceHelper;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.util.CollectionUtil;
import com.sunfusheng.github.viewmodel.BaseListViewModel;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableHelper;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng
 * @since 2020-01-07
 */
@SuppressWarnings("ALL")
abstract public class BaseListFragment<VM extends BaseListViewModel, E> extends BaseFragment implements ScrollableHelper.ScrollableViewContainer,
        RecyclerViewWrapper.OnRefreshListener,
        RecyclerViewWrapper.OnLoadMoreListener {

    protected RecyclerViewWrapper vRecyclerViewWrapper;
    protected VM mVM;
    private List<E> items = new ArrayList<>();

    @Override
    public void initData(@Nullable Bundle arguments) {
        mVM = createViewModel();
    }

    abstract protected VM createViewModel();

    abstract protected void registerViewBinders();

    @Override
    public int inflateLayout() {
        return R.layout.layout_recyclerview_wrapper;
    }

    @Override
    public void initView(@NonNull View rootView) {
        initRecyclerViewWrapper();
        registerViewBinders();
        fetchList();
    }

    private void initRecyclerViewWrapper() {
        vRecyclerViewWrapper = vRootView.findViewById(R.id.recyclerViewWrapper);
        vRecyclerViewWrapper.setLoadingLayout(R.layout.layout_loading_for_scrollable);
        vRecyclerViewWrapper.setEmptyLayout(R.layout.layout_empty_for_scrollable);

        vRecyclerViewWrapper.enableRefresh(true);
        vRecyclerViewWrapper.enableLoadMore(true);

        vRecyclerViewWrapper.setOnRefreshListener(this);
        vRecyclerViewWrapper.setOnLoadMoreListener(this);

        vRecyclerViewWrapper.setOnItemClickListener(item -> {
            onItemClick((E) item);
        });
        vRecyclerViewWrapper.setOnItemLongClickListener(item -> {
            return onItemLongClick((E) item);
        });
    }

    private void fetchList() {
        mVM.liveData.observe(this, it -> {
            ResponseData<List<E>> result = (ResponseData<List<E>>) it;

            if (DataSourceHelper.isLoading(result)) {
                if (mVM.isRefreshMode() || mVM.isLoadMode()) {
                    vRecyclerViewWrapper.enableRefresh(true);
                    vRecyclerViewWrapper.enableLoadMore(true);
                }
            } else if (DataSourceHelper.isSuccess(result)) {
                if (mVM.isRefreshMode() || mVM.isLoadMode()) {
                    items.clear();
                }
                vRecyclerViewWrapper.setLoadingState(LoadingState.SUCCESS);
                items.addAll(result.data);
                vRecyclerViewWrapper.setItems(items);
            } else if (DataSourceHelper.isError(result)) {
                if (CollectionUtil.isEmpty(items)) {
                    vRecyclerViewWrapper.setLoadingState(LoadingState.ERROR);
                } else {
                    vRecyclerViewWrapper.setLoadMoreError();
                }
            } else if (DataSourceHelper.isEmpty(result)) {
                if (CollectionUtil.isEmpty(items)) {
                    vRecyclerViewWrapper.setLoadingState(LoadingState.EMPTY);
                    vRecyclerViewWrapper.enableRefresh(false);
                    vRecyclerViewWrapper.enableLoadMore(false);
                } else {
                    vRecyclerViewWrapper.setLoadMoreEmpty();
                    vRecyclerViewWrapper.enableLoadMore(false);
                }
            }
        });
        mVM.load();
    }

    @Override
    public void onRefresh() {
        mVM.refresh();
    }

    @Override
    public void onLoadMore() {
        mVM.loadMore();
    }

    @Override
    public View getScrollableView() {
        return vRecyclerViewWrapper.getRecyclerView();
    }

    abstract protected void onItemClick(E item);

    protected boolean onItemLongClick(E item) {
        return false;
    }
}