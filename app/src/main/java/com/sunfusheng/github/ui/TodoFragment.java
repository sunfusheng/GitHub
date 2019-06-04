package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.sunfusheng.github.R;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.widget.ScrollableLayout.ScrollableHelper;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

/**
 * @author sunfusheng on 2018/7/25.
 */
public class TodoFragment extends BaseFragment implements ScrollableHelper.ScrollableViewContainer {

    private RecyclerViewWrapper recyclerViewWrapper;

    public static TodoFragment newFragment() {
        return new TodoFragment();
    }

    @Override
    public void initData(@Nullable Bundle arguments) {

    }

    @Override
    public int inflateLayout() {
        return R.layout.layout_recyclerview_wrapper;
    }

    @Override
    public void initView(@NonNull View rootView) {
        recyclerViewWrapper = rootView.findViewById(R.id.recyclerViewWrapper);

        recyclerViewWrapper.setEmptyLayout(R.layout.fragment_todo);
        recyclerViewWrapper.setLoadingState(LoadingState.EMPTY);
    }

    @Override
    public View getScrollableView() {
        return recyclerViewWrapper.getRecyclerView();
    }
}