package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.viewbinder.IssueCommentEventBinder;
import com.sunfusheng.github.viewbinder.IssueEventBinder;
import com.sunfusheng.github.viewbinder.WatchForkEventBinder;
import com.sunfusheng.github.viewmodel.EventViewModel;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class HomeFragment extends BaseFragment implements RecyclerViewWrapper.OnRefreshListener, RecyclerViewWrapper.OnLoadMoreListener {

    private RecyclerViewWrapper recyclerViewWrapper;
    private List<Object> items = new ArrayList<>();
    private static final int FIRST_PAGE = 1;
    private static final int PER_PAGE = 10;
    private int page = FIRST_PAGE;

    private String username;
    private UserViewModel userViewModel;
    private EventViewModel eventViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        observeReceivedEvents();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        toolbar.setTitle(getString(R.string.app_name_with_version, AppUtil.getVersionName()));
    }

    protected void initData() {
        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
            userViewModel = VmProvider.of(this, UserViewModel.class);
            userViewModel.setRequestParams(username, FetchMode.DEFAULT);
        }
    }

    private void initView() {
        if (getView() == null) return;
        recyclerViewWrapper = getView().findViewById(R.id.recyclerViewWrapper);
        recyclerViewWrapper.setLoadingLayout(R.layout.layout_loading_default);
        recyclerViewWrapper.getRefreshLayout().autoRefresh(100, 300, 1);
        recyclerViewWrapper.getRefreshLayout().setEnableAutoLoadMore(false);

        recyclerViewWrapper.setOnRefreshListener(this);
        recyclerViewWrapper.setOnLoadMoreListener(this);

        recyclerViewWrapper.register(Event.class, Event::getType, Event.WatchEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.ForkEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.IssuesEvent, new IssueEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.IssueCommentEvent, new IssueCommentEventBinder());
    }

    private void observeReceivedEvents() {
        eventViewModel = VmProvider.of(this, EventViewModel.class);
        eventViewModel.setRequestParams(username, page, PER_PAGE, FetchMode.LOCAL);

        eventViewModel.liveData.observe(this, it -> {
            switch (it.loadingState) {
                case LoadingState.SUCCESS:
                    if (page == FIRST_PAGE) {
                        items.clear();
                    }
                    items.addAll(it.data);
                    recyclerViewWrapper.setItems(items);
                    break;
                case LoadingState.EMPTY:
                case LoadingState.ERROR:
                    if (page <= FIRST_PAGE) {
                        recyclerViewWrapper.setLoadingState(it.loadingState);
                    } else {
                        page--;
                    }
                    break;
            }
        });
    }

    @Override
    public void onRefresh() {
        page = FIRST_PAGE;
        eventViewModel.setRequestParams(username, page, PER_PAGE, FetchMode.REMOTE);
    }

    @Override
    public void onLoadMore() {
        page++;
        eventViewModel.setRequestParams(username, page, PER_PAGE, FetchMode.REMOTE);
    }
}
