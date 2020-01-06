package com.sunfusheng.github.viewmodel;

import com.sunfusheng.github.annotation.FetchMode;
import com.sunfusheng.github.datasource.BaseDataSource;
import com.sunfusheng.github.viewmodel.params.PageParams;

/**
 * @author sunfusheng
 * @since 2020-01-06
 */
abstract public class BaseListViewModel<P extends PageParams, R> extends BaseViewModel<P, R> {
    private int mPage = PageParams.FIRST_PAGE;

    public int getPage() {
        return mPage;
    }

    public boolean isFirstPage() {
        return mPage == PageParams.FIRST_PAGE;
    }

    public int getPageCount() {
        return PageParams.PAGE_COUNT;
    }

    abstract P getPageParams();

    abstract BaseDataSource<P, R> getDataSource();

    protected void doRequest(@FetchMode int fetchMode) {
        P params = getPageParams();
        params.page = getPage();
        params.pageCount = getPageCount();
        params.fetchMode = fetchMode;
        BaseDataSource<P, R> dataSource = getDataSource();
        dataSource.setParams(params);
        request(params, dataSource);
    }

    // 下拉刷新
    public void refresh() {
        mPage = PageParams.FIRST_PAGE;
        doRequest(FetchMode.FORCE_REMOTE);
    }

    // 加载
    public void load() {
        mPage = PageParams.FIRST_PAGE;
        doRequest(FetchMode.REMOTE);
    }

    // 加载下一页
    public void loadMore() {
        mPage++;
        doRequest(FetchMode.REMOTE);
    }

    @Override
    protected void loadError(int fetchMode, int code, String msg) {
        if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
            onErrorOrEmpty();
        }
    }

    @Override
    protected void loadEmpty(int fetchMode) {
        if (fetchMode == FetchMode.REMOTE || fetchMode == FetchMode.FORCE_REMOTE) {
            onErrorOrEmpty();
        }
    }

    private void onErrorOrEmpty() {
        if (mPage > PageParams.FIRST_PAGE) {
            mPage--;
        }
    }
}
