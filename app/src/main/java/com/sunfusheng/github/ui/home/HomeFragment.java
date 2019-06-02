package com.sunfusheng.github.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.viewbinder.IssueCommentEventBinder;
import com.sunfusheng.github.viewbinder.IssueEventBinder;
import com.sunfusheng.github.viewbinder.WatchForkEventBinder;
import com.sunfusheng.github.viewmodel.ReceivedEventsViewModel;
import com.sunfusheng.github.viewmodel.base.VMProviders;
import com.sunfusheng.github.widget.SvgView;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class HomeFragment extends BaseFragment implements RecyclerViewWrapper.OnRefreshListener, RecyclerViewWrapper.OnLoadMoreListener {
    private static final int FIRST_PAGE = 1;

    private SvgView vSvgLoading;
    private View vStatusBar;
    private RecyclerViewWrapper recyclerViewWrapper;

    private List<Object> items = new ArrayList<>();
    private int mPage = FIRST_PAGE;
    private int mPageCount = Constants.PAGE_COUNT;

    private String mUsername;
    private ReceivedEventsViewModel mReceivedEventsViewModel;

    @Override
    public void initData(@Nullable Bundle arguments) {
        mUsername = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
    }

    @Override
    public int inflateLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(@NonNull View rootView) {
        vStatusBar = rootView.findViewById(R.id.statusBar);
        recyclerViewWrapper = rootView.findViewById(R.id.recyclerViewWrapper);

        initTitleBar();
        initRecyclerViewWrapper();
        fetchReceivedEvents();
    }

    private void initTitleBar() {
        ViewGroup.LayoutParams layoutParams = vStatusBar.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(AppUtil.getContext());
        vStatusBar.setLayoutParams(layoutParams);

        TextView vTitle = vRootView.findViewById(R.id.title);
        vTitle.setText(R.string.app_name);

        vSvgLoading = vRootView.findViewById(R.id.svg_loading);
        vSvgLoading.setOnStateChangeListener(null);
        vSvgLoading.setToFinishedFrame();
        vSvgLoading.setOnClickListener(v -> {
            NavigationManager.toUserActivity(getActivity(), getString(R.string.app_author_name));
        });
    }

    private void initRecyclerViewWrapper() {
        recyclerViewWrapper.setOnRefreshListener(this);
        recyclerViewWrapper.setOnLoadMoreListener(this);

        recyclerViewWrapper.register(Event.class, Event::getType, Event.WatchEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.ForkEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.IssuesEvent, new IssueEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.IssueCommentEvent, new IssueCommentEventBinder());
    }

    private void startSvgAnim() {
        vSvgLoading.setOnStateChangeListener(state -> {
            if (state == SvgView.STATE_FINISHED) {
                vSvgLoading.start();
            }
        });
        vSvgLoading.start();
    }

    private void stopSvgAnim() {
        if (vSvgLoading.getState() > SvgView.STATE_NOT_STARTED && vSvgLoading.getState() < SvgView.STATE_FINISHED) {
            vSvgLoading.setOnStateChangeListener(state -> {
                if (state == SvgView.STATE_FINISHED) {
                    vSvgLoading.setToFinishedFrame();
                }
            });
        }
    }

    private void fetchReceivedEvents() {
        mReceivedEventsViewModel = VMProviders.of(this, ReceivedEventsViewModel.class);
        mReceivedEventsViewModel.liveData.observe(this, it -> {
            Log.d("sfs", "fetchReceivedEvents() loadingState: " + it.loadingStateString + " fetchMode: " + it.fetchModeString);

            switch (it.loadingState) {
                case LoadingState.LOADING:
//                    if (!isPullToRefresh) {
//                        startSvgAnim();
//                    }
                    break;
                case LoadingState.SUCCESS:
                    if (it.fetchMode != FetchMode.LOCAL) {
                        stopSvgAnim();
                    }
                    recyclerViewWrapper.setLoadingState(it.loadingState);
                    if (mPage == FIRST_PAGE) {
                        items.clear();
                    }
                    items.addAll(it.data);
                    recyclerViewWrapper.setItems(items);
                    break;
                case LoadingState.ERROR:
                case LoadingState.EMPTY:
                    if (it.fetchMode != FetchMode.LOCAL) {
                        stopSvgAnim();
                    }
                    recyclerViewWrapper.setLoadingState(it.loadingState);
                    if (mPage > FIRST_PAGE) {
                        mPage--;
                    }
                    break;
            }
        });

        isPullToRefresh = false;
        recyclerViewWrapper.setLoadingState(LoadingState.LOADING);
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.DEFAULT);
    }

    private boolean isPullToRefresh;

    @Override
    public void onRefresh() {
        isPullToRefresh = true;
        mPage = FIRST_PAGE;
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.FORCE_REMOTE);
    }

    @Override
    public void onLoadMore() {
        mReceivedEventsViewModel.request(mUsername, mPage++, mPageCount, FetchMode.REMOTE);
    }
}
