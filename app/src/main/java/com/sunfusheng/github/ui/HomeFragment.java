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
import com.sunfusheng.github.viewbinder.WatchForkEventBinder;
import com.sunfusheng.github.viewmodel.EventViewModel;
import com.sunfusheng.github.viewmodel.UserViewModel;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

import java.util.List;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class HomeFragment extends BaseFragment {

    private RecyclerViewWrapper recyclerViewWrapper;
    private List<Object> items;
    private int page = -1;

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
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name_with_version, AppUtil.getVersionName()));
            toolbar.setSubtitle(R.string.github_url);
        }
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

        recyclerViewWrapper.register(Event.class, Event::getType, Event.WatchEvent, new WatchForkEventBinder());
        recyclerViewWrapper.register(Event.class, Event::getType, Event.ForkEvent, new WatchForkEventBinder());
    }

    private void observeReceivedEvents() {
        eventViewModel = VmProvider.of(this, EventViewModel.class);
        eventViewModel.setRequestParams(username, 1, Constants.PER_PAGE_30, FetchMode.DEFAULT);

        eventViewModel.liveData.observe(this, it -> {
            if (it.loadingState == LoadingState.SUCCESS) {
                recyclerViewWrapper.setItems(it.data);
            }
        });
    }

}
