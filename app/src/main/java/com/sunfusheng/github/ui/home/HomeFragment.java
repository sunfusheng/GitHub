package com.sunfusheng.github.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.net.response.ResponseData;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseFragment;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.CollectionUtil;
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
    private RecyclerViewWrapper recyclerViewWrapper;

    private List<Object> items = new ArrayList<>();
    private int mPage = FIRST_PAGE;
    private int mPageCount = Constants.PAGE_COUNT;

    private String mUsername;
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
            NavigationManager.toUserActivity(getActivity(), getString(R.string.app_author_name));
        });
    }

    protected void initData() {
        mUsername = PreferenceUtil.getInstance().getString(Constants.PreferenceKey.USERNAME);
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

    private void startSvgAnim() {
        if (vSvgLoading.getState() <= SvgView.STATE_NOT_STARTED || vSvgLoading.getState() >= SvgView.STATE_FINISHED) {
            vSvgLoading.setOnStateChangeListener(state -> {
                if (state == SvgView.STATE_FINISHED) {
                    vSvgLoading.start();
                }
            });
            vSvgLoading.start();
        }
    }

    private void stopSvgAnim() {
        if (vSvgLoading.getState() > SvgView.STATE_NOT_STARTED && vSvgLoading.getState() < SvgView.STATE_FINISHED) {
            vSvgLoading.setOnStateChangeListener(state -> {
                if (state == SvgView.STATE_FINISHED) {
                    vSvgLoading.setToFinishedFrame();
                }
            });
        } else {
            vSvgLoading.setToFinishedFrame();
        }
    }

    private void observeReceivedEvents() {
        mReceivedEventsViewModel = VMProviders.of(this, ReceivedEventsViewModel.class);
        mReceivedEventsViewModel.liveData.observe(this, it -> {
            Log.d("sfs", "observeReceivedEvents() loadingState: " + ResponseData.getLoadingStateString(it.loadingState));

            switch (it.loadingState) {
                case LoadingState.LOADING:
                    if (!isRefresh && !CollectionUtil.isEmpty(recyclerViewWrapper.getItems())) {
                        startSvgAnim();
                    }
                    break;
                case LoadingState.SUCCESS:
                    stopSvgAnim();
                    recyclerViewWrapper.setLoadingState(it.loadingState);
                    if (mPage == FIRST_PAGE) {
                        items.clear();
                    }
                    items.addAll(it.data);
                    recyclerViewWrapper.setItems(items);
                    break;
                case LoadingState.ERROR:
                case LoadingState.EMPTY:
                    stopSvgAnim();
                    recyclerViewWrapper.setLoadingState(it.loadingState);
                    if (mPage > FIRST_PAGE) {
                        mPage--;
                    }
                    break;
            }
        });

        isRefresh = false;
        recyclerViewWrapper.setLoadingState(LoadingState.LOADING);
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.DEFAULT);
    }

    private boolean isRefresh;

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPage = FIRST_PAGE;
        mReceivedEventsViewModel.request(mUsername, mPage, mPageCount, FetchMode.FORCE_REMOTE);
    }

    @Override
    public void onLoadMore() {
        mReceivedEventsViewModel.request(mUsername, mPage++, mPageCount, FetchMode.REMOTE);
    }
}
