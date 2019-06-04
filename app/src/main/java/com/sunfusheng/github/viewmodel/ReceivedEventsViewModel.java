package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.ReceivedEventsDataSource;
import com.sunfusheng.github.model.Event;
import com.sunfusheng.github.viewmodel.params.PageParams;
import com.sunfusheng.github.viewmodel.params.UsernamePageParams;

import java.util.List;

/**
 * @author sunfusheng on 2018/5/7.
 */
public class ReceivedEventsViewModel extends BaseViewModel<UsernamePageParams, List<Event>> {
    private String mUsername;
    private int mPage = PageParams.FIRST_PAGE;
    private boolean isRefreshRequest;

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public int getPage() {
        return mPage;
    }

    public boolean isFirstPage() {
        return mPage == PageParams.FIRST_PAGE;
    }

    public boolean isRefreshRequest() {
        return isRefreshRequest;
    }

    private void doRequest(int page, @FetchMode int fetchMode) {
        request(new UsernamePageParams(mUsername, page, PageParams.PAGE_COUNT, fetchMode),
                new ReceivedEventsDataSource(mUsername, page, PageParams.PAGE_COUNT, fetchMode));
    }

    // 下拉刷新
    public void refresh() {
        isRefreshRequest = true;
        mPage = PageParams.FIRST_PAGE;
        doRequest(mPage, FetchMode.FORCE_REMOTE);
    }

    // 加载
    public void load() {
        isRefreshRequest = false;
        mPage = PageParams.FIRST_PAGE;
        doRequest(mPage, FetchMode.REMOTE);
    }

    // 加载下一页
    public void loadMore() {
        doRequest(++mPage, FetchMode.REMOTE);
    }

    public void onErrorOrEmpty() {
        if (mPage > PageParams.FIRST_PAGE) {
            mPage--;
        }
    }
}
