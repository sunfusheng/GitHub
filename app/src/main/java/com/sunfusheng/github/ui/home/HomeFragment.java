package com.sunfusheng.github.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.response.ResponseResult;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.PreferenceUtil;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.viewbinder.IssueCommentEventBinder;
import com.sunfusheng.github.viewbinder.IssueEventBinder;
import com.sunfusheng.github.viewbinder.WatchForkEventBinder;
import com.sunfusheng.github.viewmodel.ReceivedEventsViewModel;
import com.sunfusheng.github.viewmodel.UserDetailViewModel;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.sunfusheng.github.widget.SvgView;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class HomeFragment extends BaseFragment implements RecyclerViewWrapper.OnRefreshListener, RecyclerViewWrapper.OnLoadMoreListener {

    private SvgView vSvgLoading;
    private RecyclerViewWrapper recyclerViewWrapper;

    private List<Object> items = new ArrayList<>();
    private static final int FIRST_PAGE = 1;
    private int mPage = FIRST_PAGE;
    private int mPageCount = Constants.PAGE_COUNT;

    private String mUsername;
    private UserDetailViewModel mUserDetailViewModel;
    private ReceivedEventsViewModel mReceivedEventsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar();
        initData();
        initView();
        observeReceivedEvents();
    }

    private void initTitleBar() {
        View vStatusBar = getView().findViewById(R.id.statusBar);
        ViewGroup.LayoutParams layoutParams = vStatusBar.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(AppUtil.getContext());
        vStatusBar.setLayoutParams(layoutParams);

        TextView vTitle = getView().findViewById(R.id.title);
        vTitle.setText(R.string.app_name);

        vSvgLoading = getView().findViewById(R.id.svg_loading);
        vSvgLoading.setOnStateChangeListener(null);
        vSvgLoading.setToFinishedFrame();
        vSvgLoading.setOnClickListener(v -> {
            NavigationManager.toUserActivity(getActivity(), "sunfusheng");
        });
    }

    protected void initData() {
        if (TextUtils.isEmpty(mUsername)) {
            mUsername = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
        }
        mUserDetailViewModel = VmProvider.of(this, UserDetailViewModel.class);
        mUserDetailViewModel.request(mUsername, FetchMode.DEFAULT);
    }

    private void initView() {
        if (getView() == null) return;
        recyclerViewWrapper = getView().findViewById(R.id.recyclerViewWrapper);

        recyclerViewWrapper.setOnRefreshListener(this);
        recyclerViewWrapper.setOnLoadMoreListener(this);

        recyclerViewWrapper.register(Event.class, Event::getType, Event.WatchEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.ForkEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.IssuesEvent, new IssueEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.IssueCommentEvent, new IssueCommentEventBinder());
    }

    private void observeReceivedEvents() {
        mReceivedEventsViewModel = VmProvider.of(this, ReceivedEventsViewModel.class);
        mReceivedEventsViewModel.liveData.observe(this, it -> {
            dealWithFirstLoading(it);

            if (mPage == FIRST_PAGE && (it.fetchMode == FetchMode.REMOTE || it.fetchMode == FetchMode.FORCE_REMOTE)) {
                PreferenceUtil.getInstance().put(Constants.PreferenceKey.RECEIVED_EVENTS_REFRESH_TIME, System.currentTimeMillis());
                recyclerViewWrapper.setLoadingState(it.loadingState);
            }

            Log.d("sfs", "ReceivedEventsViewModel loadingState:"+it.loadingState);

            switch (it.loadingState) {
                case LoadingState.SUCCESS:
                    if (mPage == FIRST_PAGE) {
                        items.clear();
                    }
                    items.addAll(it.data);
                    recyclerViewWrapper.setItems(items);
                    break;
                case LoadingState.ERROR:
                    if (mPage > FIRST_PAGE) {
                        mPage--;
                    }
                    break;
            }
        });

//        int fetchMode = Constants.isReceivedEventsRefreshTimeExpired() ? FetchMode.DEFAULT : FetchMode.LOCAL;
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.FORCE_REMOTE);
        isFirstLoading = true;
    }

    private boolean isFirstLoading;

    private void dealWithFirstLoading(ResponseResult response) {
        if (isFirstLoading && mReceivedEventsViewModel.getRequestFetchMode() != FetchMode.LOCAL) {
            switch (response.loadingState) {
                case LoadingState.LOADING:
                    recyclerViewWrapper.enableRefresh(false);
                    recyclerViewWrapper.enableLoadMore(false);
                    vSvgLoading.start();
                    vSvgLoading.setOnStateChangeListener(state -> {
                        if (state == SvgView.STATE_FINISHED) {
                            vSvgLoading.start();
                        }
                    });
                    break;
                case LoadingState.SUCCESS:
                case LoadingState.ERROR:
                case LoadingState.EMPTY:
                    if (response.fetchMode == FetchMode.REMOTE) {
                        recyclerViewWrapper.enableRefresh(true);
                        recyclerViewWrapper.enableLoadMore(true);
                        vSvgLoading.setOnStateChangeListener(null);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        isFirstLoading = false;
        mPage = FIRST_PAGE;
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.FORCE_REMOTE);
    }

    @Override
    public void onLoadMore() {
        isFirstLoading = false;
        mPage++;
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.REMOTE);
    }
}
